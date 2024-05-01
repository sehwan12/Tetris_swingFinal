package IO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.*;

public class ScoreIOTest {

    private static final String JSON_FILE = "userScore.json";
    private static String originalContent;

    @BeforeAll
    static void setUpBeforeClass() throws IOException {
        // Save the original content of the JSON file
        originalContent = Files.readString(Paths.get(JSON_FILE));
    }

    @AfterAll
    static void tearDownAfterClass() throws IOException {
        // Restore the original content of the JSON file
        Files.write(Paths.get(JSON_FILE), originalContent.getBytes());
    }

    @BeforeEach
    void setUp() {
        // Clear the JSON file before each test
        ScoreIO.clearJsonFile();
    }

    @Test
    void testWriteFirstScore_EmptyFile() {
        // Write the first score to an empty file
        ScoreIO.writeFirstScore("TestUser", 1000, "Easy", "Mode1");

        // Verify that the score is written correctly
        JSONArray jsonArray = readJsonFile();
        assertEquals(1, jsonArray.size());
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
        assertEquals("TestUser", jsonObject.get("name"));
        assertEquals(1000L, jsonObject.get("score"));
        assertEquals("Easy", jsonObject.get("difficulty"));
        assertEquals("Mode1", jsonObject.get("mode"));
    }

    @Test
    void testWriteAfterScore_NonEmptyFile() {
        // Write some initial scores to the file
        ScoreIO.writeFirstScore("ExistingUser", 500, "Medium", "Mode2");

        // Write a new score to the file
        ScoreIO.writeAfterScore("NewUser", 1500, "Hard", "Mode3");

        // Verify that both scores are present in the file
        JSONArray jsonArray = readJsonFile();
        assertEquals(2, jsonArray.size());

        JSONObject firstScore = (JSONObject) jsonArray.get(0);
        assertEquals("ExistingUser", firstScore.get("name"));

        JSONObject secondScore = (JSONObject) jsonArray.get(1);
        assertEquals("NewUser", secondScore.get("name"));
    }

    @Test
    void testClearJsonFile() {
        // Write some scores to the file
        ScoreIO.writeFirstScore("TestUser", 1000, "Easy", "Mode1");
        ScoreIO.writeAfterScore("AnotherUser", 2000, "Hard", "Mode2");

        // Clear the file
        ScoreIO.clearJsonFile();

        // Verify that the file is empty
        JSONArray jsonArray = readJsonFile();
        assertTrue(jsonArray.isEmpty());
    }

    @Test
    void testIsFileEmpty() {
        // Initially, the file is empty
        assertTrue(ScoreIO.isFileEmpty());

        // Write a score to the file
        ScoreIO.writeFirstScore("TestUser", 1000, "Easy", "Mode1");

        // Now the file is not empty
        assertFalse(ScoreIO.isFileEmpty());
    }

    // Helper method to read the content of the JSON file
    @SuppressWarnings("unchecked")
    private JSONArray readJsonFile() {
        try (FileReader reader = new FileReader(JSON_FILE)) {
            JSONParser parser = new JSONParser();
            return (JSONArray) parser.parse(reader);
        } catch (IOException | ParseException e) {
            //e.printStackTrace();
            return new JSONArray();
        }
    }
}