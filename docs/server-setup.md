# Deployment

## Architektur

whenly laeuft auf demselben Hetzner-Server wie ev-monitor.net (`46.225.210.231`).

- **whenly-Stack** (`docker-compose.prod.yml`): postgres, backend, frontend, nginx - gebunden auf `0.0.0.0:8091`
- **ev-monitor nginx** (Port 80/443): terminiert SSL fuer whenly.de, proxied zu `172.18.0.1:8091`
- **Cert-Renewal**: laeuft automatisch im ev-monitor certbot-Container (shared Volume)

```
Internet -> ev-monitor nginx (:443) -> whenly nginx (:8091) -> frontend/backend
```

## Laufender Betrieb

**Deploy:** Jeder Push auf `main` deployt automatisch via GitHub Actions (SSH -> `git pull` + `deploy.sh`).

**Manueller Deploy:**
```bash
ssh -i ~/.ssh/ihle-private -p 2222 ihle@46.225.210.231
cd /opt/whenly/whenly && ./deploy.sh
```

**Logs:**
```bash
docker compose -f docker-compose.prod.yml logs -f backend
docker compose -f docker-compose.prod.yml logs -f nginx
```

**Health Check:**
```bash
curl http://127.0.0.1:8091/actuator/health
```

## Server-Konfiguration

**Pfade:**
- Repo: `/opt/whenly/whenly/`
- Env: `/opt/whenly/whenly/.env`

**ev-monitor nginx Volume-Mount** (in `docker-compose.full.yml`):
```yaml
- /opt/whenly/whenly/nginx/whenly-ev-monitor.conf:/etc/nginx/conf.d/whenly.conf:ro
```

**GitHub Secrets:**

| Secret | Wert |
|--------|------|
| `SERVER_HOST` | `46.225.210.231` |
| `SERVER_USER` | `ihle` |
| `SSH_PRIVATE_KEY` | `~/.ssh/ihle-private` |

## Ersteinrichtung (einmalig)

Nur relevant wenn der Server neu aufgesetzt wird:

1. Repo klonen: `mkdir -p /opt/whenly && git clone ... /opt/whenly/whenly`
2. `.env` anlegen (siehe `.env.example`) - `WHENLY_DB_PASSWORD` und `WHENLY_BASE_URL=https://whenly.de`
3. DNS: A-Record `whenly.de` -> `46.225.210.231`, AAAA -> `2a01:4f8:1c19:a28e::1`
4. Volume-Mount in ev-monitor `docker-compose.full.yml` eintragen, nginx neu starten
5. Temp. HTTP-Config fuer ACME aktivieren (siehe `nginx/whenly-ev-monitor.conf` - `listen [::]:80` benoetigt)
6. Cert ausstellen:
   ```bash
   cd /opt/ev-monitor/ev-monitor
   docker compose -f docker-compose.full.yml run --rm --entrypoint certbot certbot \
     certonly --webroot -w /var/www/certbot \
     -d whenly.de -d www.whenly.de \
     --email sebastian.wien@posteo.com --agree-tos --non-interactive
   ```
7. SSL-Config wiederherstellen: `git checkout nginx/whenly-ev-monitor.conf`, ev-monitor nginx neu starten
8. `./deploy.sh` fuer ersten Stack-Start
