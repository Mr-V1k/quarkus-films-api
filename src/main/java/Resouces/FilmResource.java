package Resouces;

import Repository.FilmRepository;
import jakarta.inject.Inject;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import model.Film;

import javax.print.attribute.standard.Media;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/")
public class FilmResource {

    @Inject
    FilmRepository filmRepository;

    @GET
    @Path("/film")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(){
        return "Hello World!";
    }

    @GET
    @Path("/film/{filmId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getFilm(short filmId){
        Optional<Film> film = filmRepository.getFilmByID(filmId);
        return film.isPresent() ? film.get().getTitle() : "No film was found with that id";
    }

    @GET
    @Path("/film/{page}/{minLength}")
    @Produces(MediaType.TEXT_PLAIN)
    public String paged(long page, short minLength){
        return filmRepository.paged(page, minLength)
                .map(f -> String.format("%s (%d min)", f.getTitle(), f.getLength()))
                .collect(Collectors.joining("\n"));
    }

    @GET
    @Path("/film/actors/{minLength}")
    @Produces(MediaType.TEXT_PLAIN)
    public String actors(String minLength){
        return filmRepository.actors(minLength)
                .map(
                        f -> String.format("%s (%d min): %s",
                                f.getTitle(),
                                f.getLength(),
                                f.getActors().stream()
                                        .map(a -> String.format("%s %s", a.getFirstName(), a.getLastName()))
                                        .collect(Collectors.joining("\n"))))
                .collect(Collectors.joining("\n"));
    }

    @POST
    @Path("/film/update/{minLength}/{rentalRate}")
    @Produces(MediaType.TEXT_PLAIN)
    public String updateRentalRate(short minLength, Float rentalRate){
        filmRepository.updateRentalPrices(minLength, rentalRate);
        return filmRepository.getFilms(minLength)
                .map(f -> String.format("%s (%d min) - $%f", f.getTitle(), f.getLength(), f.getRentalRate()))
                .collect(Collectors.joining("\n"));
    }

}
