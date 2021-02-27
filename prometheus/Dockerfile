FROM prom/prometheus:v2.24.0

COPY prometheus.yml /etc/prometheus/

CMD [ "--config.file=/etc/prometheus/prometheus.yml", \
      "--storage.tsdb.path=/prometheus", \
      "--web.console.libraries=/usr/share/prometheus/console_libraries", \
      "--web.console.templates=/usr/share/prometheus/consoles",\
      "--storage.tsdb.retention.size=5GB" ]
