package net.whenly.poll;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.whenly.common.ApiException;
import net.whenly.config.WhenlyProperties;
import net.whenly.domain.Poll;
import net.whenly.domain.PollOption;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/polls/{publicId}")
public class ExportController {

  private final PollService pollService;
  private final WhenlyProperties properties;

  public ExportController(PollService pollService, WhenlyProperties properties) {
    this.pollService = pollService;
    this.properties = properties;
  }

  @GetMapping("/ics")
  public ResponseEntity<byte[]> ics(@PathVariable String publicId) {
    Poll poll = pollService.getByPublicId(publicId);
    UUID finalizedId = poll.getFinalizedOptionId();
    if (finalizedId == null) {
      throw ApiException.conflict("not_finalized", "Poll is not finalized yet");
    }
    PollOption option = poll.getOptions().stream()
        .filter(o -> o.getId().equals(finalizedId))
        .findFirst()
        .orElseThrow(() -> ApiException.notFound("finalized_option_missing"));

    ICalendar ical = new ICalendar();
    VEvent event = new VEvent();
    event.setSummary(poll.getTitle());
    if (poll.getDescription() != null) event.setDescription(poll.getDescription());
    if (poll.getLocation() != null) event.setLocation(poll.getLocation());
    Instant start = option.getStartAt() != null ? option.getStartAt() : Instant.now();
    Instant end = option.getEndAt() != null
        ? option.getEndAt()
        : start.plus(1, ChronoUnit.HOURS);
    event.setDateStart(Date.from(start));
    event.setDateEnd(Date.from(end));
    event.setUrl(properties.baseUrl() + "/p/" + poll.getPublicId());
    ical.addEvent(event);

    byte[] bytes = Biweekly.write(ical).go().getBytes(StandardCharsets.UTF_8);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType("text/calendar; charset=UTF-8"));
    headers.setContentDisposition(ContentDisposition.attachment()
        .filename(safeFilename(poll.getTitle()) + ".ics")
        .build());
    return new ResponseEntity<>(bytes, headers, 200);
  }

  @GetMapping(value = "/qr", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<byte[]> qr(@PathVariable String publicId) {
    Poll poll = pollService.getByPublicId(publicId);
    String url = properties.baseUrl() + "/p/" + poll.getPublicId();
    try {
      Map<EncodeHintType, Object> hints = new HashMap<>();
      hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
      hints.put(EncodeHintType.MARGIN, 2);
      BitMatrix matrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 512, 512, hints);
      try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(out.toByteArray());
      }
    } catch (IOException | com.google.zxing.WriterException e) {
      throw new RuntimeException("QR generation failed", e);
    }
  }

  private static String safeFilename(String title) {
    String s = title.replaceAll("[^a-zA-Z0-9._-]+", "_");
    if (s.length() > 60) s = s.substring(0, 60);
    return s.isBlank() ? "event" : s;
  }
}
