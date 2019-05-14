// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// [START sheets_quickstart]
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;

import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

public class Stats {
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart. If modifying these
     * scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * 
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = Stats.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                clientSecrets, SCOPES)
                        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                        .setAccessType("offline").build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private static void saveStatsHillClimb(String valueRange, String timeRange, Sheets service, String spreadsheetId)
            throws IOException {
        for (int i = 6; i < 7; i++) {
            Problem problem = new Problem("res/exam_comp_set" + i + ".exam");
            ArrayList<List<Object>> times = new ArrayList<List<Object>>();
            ArrayList<List<Object>> values = new ArrayList<List<Object>>();

            for (int j = 0; j < 2; j++) {
                ArrayList<Exam> solution = new ArrayList<Exam>();

                long startTime = System.currentTimeMillis();

                if (valueRange.contains("HILLCLIMB"))
                    solution = HillClimb.solve(problem);
                else if (valueRange.contains("ANNEALING"))
                    solution = SimulatedAnnealing.solve(problem);

                long stopTime = System.currentTimeMillis();

                int value = problem.evaluate(solution);
                float elapsedTime = (stopTime - startTime) / 1000F;
                times.add(Arrays.asList(elapsedTime));
                values.add(Arrays.asList(value));
            }

            ValueRange body = new ValueRange().setValues(times);
            UpdateValuesResponse result = service.spreadsheets().values().update(spreadsheetId, timeRange, body)
                    .setValueInputOption("RAW").execute();
            System.out.printf("%d cells updated.", result.getUpdatedCells());

            body = new ValueRange().setValues(values);
            result = service.spreadsheets().values().update(spreadsheetId, valueRange, body).setValueInputOption("RAW")
                    .execute();
            System.out.printf("%d cells updated.", result.getUpdatedCells());
        }
    }

    private static void saveStatsGenetic(Sheets service, String spreadsheetId) throws IOException {
        String valueRange = "GENETIC!E3:E9";
        String timeRange = "GENETIC!C3:C9";

        for (int i = 6; i < 7; i++) {
            Problem problem = new Problem("res/exam_comp_set" + i + ".exam");
            ArrayList<List<Object>> times = new ArrayList<List<Object>>();
            ArrayList<List<Object>> values = new ArrayList<List<Object>>();

            for (int j = 0; j < 2; j++) {
                ArrayList<Exam> solution = new ArrayList<Exam>();

                long startTime = System.currentTimeMillis();

                if (valueRange.contains("HILLCLIMB"))
                    solution = HillClimb.solve(problem);
                else if (valueRange.contains("ANNEALING"))
                    solution = SimulatedAnnealing.solve(problem);

                long stopTime = System.currentTimeMillis();

                int value = problem.evaluate(solution);
                float elapsedTime = (stopTime - startTime) / 1000F;
                times.add(Arrays.asList(elapsedTime));
                values.add(Arrays.asList(value));
            }

            ValueRange body = new ValueRange().setValues(times);
            UpdateValuesResponse result = service.spreadsheets().values().update(spreadsheetId, timeRange, body)
                    .setValueInputOption("RAW").execute();
            System.out.printf("%d cells updated.", result.getUpdatedCells());

            body = new ValueRange().setValues(values);
            result = service.spreadsheets().values().update(spreadsheetId, valueRange, body).setValueInputOption("RAW")
                    .execute();
            System.out.printf("%d cells updated.", result.getUpdatedCells());
        }
    }

    /**
     * Prints the names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1gAnnrwxZvXVDLvXEUKtFiGTk1Om_h_c4dOgdsPKxv6Y/edit
     */
    public static void main(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1gAnnrwxZvXVDLvXEUKtFiGTk1Om_h_c4dOgdsPKxv6Y";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME).build();

        saveStatsHillClimb("ANNEALING!E3:E9", "ANNEALING!C3:C9", service, spreadsheetId);
        // saveStatsHillClimb("HILLCLIMB!E3:E9", "HILLCLIMB!C3:C9", service,
        // spreadsheetId);
        saveStatsGenetic(service, spreadsheetId);
    }
}
// [END sheets_quickstart]
