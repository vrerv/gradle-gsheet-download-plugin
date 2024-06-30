package com.vrerv.lib.googlecloud.sheets;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Slf4j
public class GoogleSheetToCSV {

	private final String APPLICATION_NAME = "Google Sheets API Java";
	private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

	private GoogleCredentials credentials;

	public void loadGoogleCredentials() throws IOException {
		credentials = getGoogleCredentials(null);
	}

	public void loadGoogleCredentials(@Nullable String serviceJsonPath) throws IOException {
		credentials = getGoogleCredentials(serviceJsonPath);
	}

	public ByteArrayInputStream getCsvInputStream(String sheetId, String range) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
		writeCsv(sheetId, range, writer);

		return new ByteArrayInputStream(out.toByteArray());
	}

    public void writeCsv(String spreadsheetId, String range, Writer writer) throws IOException {
		if (credentials == null) {
			throw new IllegalStateException("Google credentials not loaded");
		}
		final Sheets service;
		try {
			service = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, new HttpCredentialsAdapter(credentials))
					.setApplicationName(APPLICATION_NAME)
					.build();
		}
		catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}

		ValueRange response = service.spreadsheets().values()
				.get(spreadsheetId, range)
				.execute();
		List<List<Object>> values = response.getValues();

		if (values == null || values.isEmpty()) {
			throw new IllegalArgumentException("No data found");
		}

		try (CSVWriter csvWriter = new CSVWriter(writer)) {

			int maxColumns = values.get(0).size(); // Get the number of columns from the header row

			values.stream().map(it -> mapRowValues(it, maxColumns))
					.forEach(csvWriter::writeNext);
		}
	}

	private GoogleCredentials getGoogleCredentials(@Nullable String serviceJsonPath) throws IOException {
		GoogleCredentials credentials;

		if (serviceJsonPath == null) {
			credentials = GoogleCredentials.getApplicationDefault()
					.createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS_READONLY));
		} else {
			InputStream inputStream = serviceJsonPath.startsWith("classpath:")
					? getClass().getClassLoader().getResourceAsStream(serviceJsonPath.substring("classpath:".length()))
					: Files.newInputStream(Paths.get(serviceJsonPath));
			if (inputStream == null) {
				throw new FileNotFoundException("Service account file not found: " + serviceJsonPath);
			}
			credentials = GoogleCredentials.fromStream(inputStream)
					.createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS_READONLY));
		}
		return credentials;
	}

	private static String[] mapRowValues(List<Object> row, int maxColumns) {
		String[] rowArray = new String[maxColumns];
		for (int i = 0; i < maxColumns; i++) {
			if (i < row.size()) {
				rowArray[i] = row.get(i).toString();
			} else {
				rowArray[i] = ""; // Fill with empty string if no value
			}
		}
		return rowArray;
	}
}
