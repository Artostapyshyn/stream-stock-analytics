# JMeter load profiles

The profiles target `GET http://localhost:9003/api/v1/users` by default. The
host, port, and path can be overridden with `-Jtarget_host`, `-Jtarget_port`,
and `-Jtarget_path`.

Install the **Throughput Shaping Timer** from JMeter Plugins before running:

```bash
jmeter-plugins-manager --install-for-jmeter throughput-shaping-timer
```

Run from the repository root in non-GUI mode:

```bash
jmeter -n -t performance-tests/jmeter/scenario-ramp-up.jmx \
  -Jtarget_host=localhost -Jtarget_port=9003 \
  -l results-ramp-up.jtl -e -o report-ramp-up

jmeter -n -t performance-tests/jmeter/scenario-spike.jmx \
  -Jtarget_host=localhost -Jtarget_port=9003 \
  -l results-spike.jtl -e -o report-spike

jmeter -n -t performance-tests/jmeter/scenario-soak.jmx \
  -Jtarget_host=localhost -Jtarget_port=9003 \
  -l results-soak.jtl -e -o report-soak
```

The ramp-up profile lasts 11 minutes including the one-minute worker ramp-up,
the spike profile lasts 10 minutes, and the soak profile lasts 2 hours plus
one minute for worker ramp-up. The timer schedules are 10-500 RPS, 50 RPS
with 10-second spikes to 500 RPS every 120 seconds, and steady 150 RPS.
