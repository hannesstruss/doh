package main

import (
	"fmt"
	"log"
	"net/http"
	"os"
	"bufio"
	"strings"
	"strconv"
)

func index(w http.ResponseWriter, r *http.Request) {
	if r.URL.Path != "/" {
		http.NotFound(w, r)
		return
	}
	fmt.Fprintf(w, "Hello there!")
}

func writeMetric(w http.ResponseWriter, name string, help string, value float64) {
	fmt.Fprintf(w, "# HELP %s %s\n", name, help)
	fmt.Fprintf(w, "# TYPE %s gauge\n", name)
	fmt.Fprintf(w, "%s %f\n\n", name, value)
}

func getMemory() (uint64, uint64) {
	file, err := os.Open("/proc/meminfo")
	if err != nil {
		log.Fatal(err)
	}
	defer file.Close()

	var totalMem uint64
	var availableMem uint64

	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		line := scanner.Text()
		parts := strings.Fields(line)

		field := strings.TrimSuffix(parts[0], ":")
		value, err := strconv.ParseUint(parts[1], 10, 64)

		if err == nil {
			if field == "MemTotal" {
				totalMem = value
			} else if field == "MemAvailable" {
				availableMem = value
			}
		}
	}

	return totalMem, availableMem
}

func metrics(w http.ResponseWriter, r *http.Request) {
	totalMem, availableMem := getMemory()
	writeMetric(w, "doh32_memory_available_kb", "The used memory on the RPi in kb.", float64(availableMem))
	writeMetric(w, "doh32_memory_total_kb", "The total available memory on the RPi in kb.", float64(totalMem))
}

func main() {
	fmt.Println("Starting up system-metrics...")
	http.HandleFunc("/", index)
	http.HandleFunc("/metrics", metrics)
	log.Fatal(http.ListenAndServe(":8080", nil))
}
