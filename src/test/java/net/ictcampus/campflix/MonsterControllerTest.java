package net.ictcampus.campflix;


import net.ictcampus.campflix.controller.controllers.MovieController;
import net.ictcampus.campflix.controller.services.MovieService;
import net.ictcampus.campflix.controller.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.TimeZone;

import static net.ictcampus.campflix.utils.TestDataUtil.getTestMovies;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MovieController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MonsterControllerTest {

    // JSON-Gerüst für alle Filme (entspricht dem Gerüst von TestDataUtil: getTestMovies())
    private static final String JSON_ALL_MOVIES = "[{\"id\":1, \"name\": \"Movie1\", \"duration\": 120, \"genre\": { \"id\": 1, \"name\": \"Melodrama\"}}, " +
            "{\"id\":2, \"name\": \"Movie2\", \"duration\": 120, \"genre\": { \"id\": 1, \"name\": \"Melodrama\"}}, "+
            "{\"id\":3, \"name\": \"Movie3\", \"duration\": 120, \"genre\": { \"id\": 1, \"name\": \"Melodrama\"}}]";

    private static final String JSON_ALL_MONSTERS = "[{\"id\":1, \"name\": \"Chimera\", \"characteristic\": \"it is something of a mix.\"}, " +
            "{\"id\":2, \"name\": \"Skylla\", \"characteristic\": \"Way to many head\"}, "+
            "{\"id\":3, \"name\": \"Minotaur\", \"characteristic\": \"Another brat of Zeus?\"}]";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    // muss mitgegeben werden, da wir für Requests angemeldet sein müssen
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    // dieser kommt zusätzlich mit, da das Passwort verschlüsselt und entschlüsselt wird
    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // vor jedem Testfall wird die Zeitzone gemäss der DB-Einstellungen gesetzt, ansonsten könnte es
    // zu Fehler bei der Abfrage kommen
    @BeforeEach
    public void prepare() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    /**
     * GET-Request: Alle Filme werden herausgegeben und getestet, ob sie im richtigen JSON-Format
     * geschickt werden
     * @throws Exception
     */
    @Test
    public void checkGet_whenNoParam_thenAllMoviesReturned() throws Exception {
        // gibt alle Filme aus, sobald findAll im gemockten MovieService aufgerufen wird
        doReturn(getTestMovies()).when(movieService).findAll();

        // GET-Request über localhost:8080/movies "geschickt"
        mockMvc.perform(get("/movies"))
                // 200 (OK) wird erwartet -> bei erfolgreicher Abfrage, bekommen wir in der Regel
                // den Statuscode 200 zurück
                .andExpect(status().isOk())
                // wir erwarten, dass der Inhalt der Abfrage mit unserem JSON-Gerüst (unsere oben
                // definierte Konstante) übereinstimmt
                .andExpect(content().json(JSON_ALL_MOVIES));
    }
    

    /**
     *  GET-Request: Der richtige Film über name-Query wird getestet
     * @throws Exception
     */
    @Test
    public void checkGet_whenValidName_thenMovieIsReturned() throws Exception {
        // lokale Variable für den Filmname, dass variabel getestet werden kann
        String movieName = "Movie3";

        // gibt den Film "Movie3" aus sobald findByName im MovieService aufgerufen wird
        doReturn(getTestMovies().subList(2, 3)).when(movieService).findByName(movieName);

        // GET-Request über localhost:8080/movies "geschickt"
        mockMvc.perform(get("/movies")
                // unserer URL wird zusätzlich ein Query-Parameter mitgegeben (unser Moviename)
                // -> localhost:8080/movies?name=Movie3
                .queryParam("name", movieName))
                // Statuscode 200 (OK) wird erwartet
                .andExpect(status().isOk())
                // auf der ersten Ebene des JSONs wird erwartet, dass der Name Movie3 auftaucht
                .andExpect(jsonPath("$[0].name").value(movieName));
    }

    /**
     * POST-Request: neuer Film wird geaddet und überprüft
     * @throws Exception
     */
    @Test
    public void checkPost_whenNewMovie_thenIsOk() throws Exception {

        // POST-Request über localhost:8080/movies "geschickt"
        mockMvc.perform(post("/movies")
                // der Inhalt in unserem Body entspricht einem JSON
                .contentType("application/json")
                // ein neues Movie-Objekt wird als JSON in den Body gegeben und mitgeschickt
                .content("{\"id\":99, \"name\": \"NewMovie\", \"duration\": 135}"))
                // wir erwarten den Status 201 (CREATED)
                .andExpect(status().isCreated());
    }

    /**
     *  DELETE-Request: Film mit der ID 1 wird gelöscht und überprüft
     * @throws Exception
     */
    @Test
    public void checkDelete_whenIdGiven_thenIsOk() throws Exception {
        // DELETE-Request über localhost:8080/movies/1 wird "ausgeführt"
        mockMvc.perform(delete("/movies/1"))
                // Status 200 (OK) wird erwartet
                .andExpect(status().isOk());

        // über Mockito wird verifiziert, ob die ID bei deleteById der ID 1 entspricht
        Mockito.verify(movieService).deleteById(eq(1));
    }
}
