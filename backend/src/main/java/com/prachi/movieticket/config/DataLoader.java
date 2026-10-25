package com.prachi.movieticket.config;

import com.prachi.movieticket.entity.Movie;
import com.prachi.movieticket.entity.Show;
import com.prachi.movieticket.repository.MovieRepository;
import com.prachi.movieticket.repository.ShowRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/** Inserts sample movies and shows on first start (only when the database is empty). */
@Component
public class DataLoader implements CommandLineRunner {

    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;

    public DataLoader(MovieRepository movieRepository, ShowRepository showRepository) {
        this.movieRepository = movieRepository;
        this.showRepository = showRepository;
    }

    @Override
    public void run(String... args) {
        if (movieRepository.count() > 0) return;

        List<Movie> movies = movieRepository.saveAll(List.of(
                new Movie("The Last Orbit", "An astronaut stranded on a failing space station races against time to return home.",
                        "Sci-Fi", "English", 142, "UA", "https://picsum.photos/seed/orbit/400/600", LocalDate.now().minusDays(10)),
                new Movie("Gully Cricket", "A group of friends from a small town chase their dream of playing professional cricket.",
                        "Drama", "Hindi", 128, "U", "https://picsum.photos/seed/cricket/400/600", LocalDate.now().minusDays(3)),
                new Movie("Wada No. 7", "A family moves into an old wada in Pune and uncovers its secrets.",
                        "Thriller", "Marathi", 117, "UA", "https://picsum.photos/seed/wada/400/600", LocalDate.now().minusDays(1)),
                new Movie("Laugh Riot", "Two rival stand-up comedians are forced to share one stage for a month.",
                        "Comedy", "Hindi", 110, "U", "https://picsum.photos/seed/laugh/400/600", LocalDate.now())
        ));

        String[] theaters = {"PVR Phoenix, Pune", "INOX City Centre, Nanded"};
        double[] prices = {250, 180};
        LocalTime[] times = {LocalTime.of(10, 30), LocalTime.of(14, 0), LocalTime.of(18, 30), LocalTime.of(21, 45)};

        for (Movie movie : movies) {
            for (int day = 0; day < 3; day++) {
                for (int t = 0; t < theaters.length; t++) {
                    LocalDateTime time = LocalDateTime.of(LocalDate.now().plusDays(day + 1), times[(day + t) % times.length]);
                    showRepository.save(new Show(movie, theaters[t], "Screen " + (t + 1), time, prices[t], 8, 12));
                }
            }
        }
    }
}
