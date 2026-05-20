# Server Setup (einmalig)

whenly laeuft auf demselben Hetzner-Server wie ev-monitor.net.
SSH: `ssh -i ~/.ssh/ihle-private -p 2222 ihle@46.225.210.231`

## 1. Repo klonen

```bash
mkdir -p /opt/whenly
cd /opt/whenly
git clone https://github.com/DEIN_ORG/whenly.git whenly
cd whenly
chmod +x deploy.sh
```

## 2. .env anlegen

```bash
cp .env.example .env
nano .env
# WHENLY_DB_PASSWORD=<sicheres Passwort>
# WHENLY_BASE_URL=https://whenly.de
```

## 3. Let's Encrypt Cert ausstellen

Das Cert muss im ev-monitor certbot-Container beantragt werden, der den ACME-Challenge-Ordner bedient:

```bash
cd /opt/ev-monitor/ev-monitor

# Temporaer HTTP fuer ACME freigeben (ev-monitor nginx leitet bereits /.well-known durch)
# whenly.de muss DNS A-Record auf 46.225.210.231 haben

docker compose -f docker-compose.full.yml exec certbot \
  certbot certonly --webroot \
  -w /var/www/certbot \
  -d whenly.de -d www.whenly.de \
  --email deine@email.de \
  --agree-tos --non-interactive
```

## 4. whenly-nginx in ev-monitor einhaengen

In `/opt/ev-monitor/ev-monitor/docker-compose.full.yml` unter `nginx` > `volumes` ergaenzen:

```yaml
- /opt/whenly/whenly/nginx/whenly-ev-monitor.conf:/etc/nginx/conf.d/whenly.conf:ro
```

Dann ev-monitor nginx neu starten:

```bash
cd /opt/ev-monitor/ev-monitor
docker compose -f docker-compose.full.yml restart nginx
```

## 5. Ersten Deploy starten

```bash
cd /opt/whenly/whenly
./deploy.sh
```

## 6. GitHub Secrets setzen

Im GitHub-Repo unter Settings > Secrets > Actions:

| Secret | Wert |
|--------|------|
| `SERVER_HOST` | `46.225.210.231` |
| `SERVER_USER` | `ihle` |
| `SSH_PRIVATE_KEY` | Inhalt von `~/.ssh/ihle-private` |

Ab dann deployt jeder Push auf `main` automatisch.
