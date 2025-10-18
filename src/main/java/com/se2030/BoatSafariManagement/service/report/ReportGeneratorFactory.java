package com.se2030.BoatSafariManagement.service.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ReportGeneratorFactory {

    private final List<ReportGenerator> reportGenerators;

    @Autowired
    public ReportGeneratorFactory(List<ReportGenerator> reportGenerators) {
        this.reportGenerators = reportGenerators;
    }

    public ReportGenerator getGenerator(String format) {
        Optional<ReportGenerator> generator = reportGenerators.stream()
                .filter(g -> g.getSupportedFormat().equalsIgnoreCase(format))
                .findFirst();

        return generator.orElseThrow(() ->
                new IllegalArgumentException("No report generator found for format: " + format));
    }
}

