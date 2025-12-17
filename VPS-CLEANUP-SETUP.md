# VPS Cleanup Automation Setup

## 1. Copy cleanup scripts to VPS
```bash
# Copy to VPS
scp docker-cleanup.sh vps-maintenance.sh your-vps:/root/
```

## 2. Set up journal limits
```bash
# On VPS - copy journal config
sudo cp journald.conf /etc/systemd/journald.conf
sudo systemctl restart systemd-journald
```

## 3. Set up automated cleanup (weekly)
```bash
# On VPS - add to crontab
(crontab -l 2>/dev/null; echo "0 2 * * 0 /root/vps-maintenance.sh") | crontab -
```

## 4. Make scripts executable
```bash
chmod +x /root/docker-cleanup.sh
chmod +x /root/vps-maintenance.sh
```

## What this prevents:
- ✅ Docker images: Limited to 30MB logs per container (10MB × 3 files)
- ✅ System journals: Limited to 100MB total, 7 days retention
- ✅ Old Docker images: Weekly cleanup via cron
- ✅ Rotated logs: Automatic removal of old .1, .gz files
- ✅ Package cache: Weekly cleanup

## Manual cleanup anytime:
```bash
./docker-cleanup.sh        # Clean Docker waste
./vps-maintenance.sh      # Full system cleanup
```