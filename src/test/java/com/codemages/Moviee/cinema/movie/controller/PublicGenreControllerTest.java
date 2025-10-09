package com.codemages.Moviee.cinema.movie.controller;

import com.codemages.Moviee.cinema.factory.CinemaFactory;
import com.codemages.Moviee.cinema.movie.assembler.GenreModelAssembler;
import com.codemages.Moviee.cinema.movie.dto.GenreResponseDTO;
import com.codemages.Moviee.cinema.movie.service.GenreService;
import com.codemages.Moviee.core.constant.ControllerConstant;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PublicGenreController.class)
@Import(GenreModelAssembler.class)
@AutoConfigureMockMvc(addFilters = false)
public class PublicGenreControllerTest {
  private static final String BASE_URL = ControllerConstant.PUBLIC_API_BASE + "/v1/genres";

  @MockitoBean
  private GenreService genreService;

  @Autowired
  private MockMvc mvc;

  @ParameterizedTest(name = "Accessing as {0}")
  @CsvSource({
    "USER", "MODERATOR", "ADMIN"
  })
  @DisplayName("Should return 200 and a list of genres when any type of user try to get all genres")
  void getAllGenres_AnyTypeOfUser_ShouldReturnOk(String role) throws Exception {
    List<GenreResponseDTO> genres = List.of(
      CinemaFactory.createGenreResponseDTO(),
      CinemaFactory.createGenreResponseDTO(),
      CinemaFactory.createGenreResponseDTO()
    );
    when( genreService.findAll() ).thenReturn( genres );

    GenreResponseDTO genre = genres.getFirst();

    mvc.perform( get( BASE_URL ).with( SecurityMockMvcRequestPostProcessors.user( role )
        .roles( role ) ) )
      .andExpect( status().isOk() )
      .andExpect( content().contentType( MediaTypes.HAL_JSON_VALUE ) )
      .andExpect( jsonPath( "$._embedded.genres", Matchers.hasSize( 3 ) ) )
      .andExpect( jsonPath(
        "$._embedded.genres[0].id",
        Matchers.equalTo( genre.id().intValue() )
      ) )
      .andExpect( jsonPath( "$._embedded.genres[0].name", Matchers.equalTo( genre.name() ) ) )
      .andExpect( jsonPath(
        "$._embedded.genres[1].description",
        Matchers.equalTo( genre.description() )
      ) )
      .andExpect( jsonPath( "$._links.genres.href", Matchers.notNullValue() ) );
  }

  @Test
  @DisplayName("Should return 200 and a list of genres when any type of user try to get all genres")
  @WithAnonymousUser
  void getAllGenres_AnonymousUser_ShouldReturnOk() throws Exception {
    List<GenreResponseDTO> genres = List.of(
      CinemaFactory.createGenreResponseDTO(),
      CinemaFactory.createGenreResponseDTO(),
      CinemaFactory.createGenreResponseDTO()
    );
    when( genreService.findAll() ).thenReturn( genres );

    GenreResponseDTO genre = genres.getFirst();

    mvc.perform( get( BASE_URL ) )
      .andExpect( status().isOk() )
      .andExpect( content().contentType( MediaTypes.HAL_JSON_VALUE ) )
      .andExpect( jsonPath( "$._embedded.genres", Matchers.hasSize( 3 ) ) )
      .andExpect( jsonPath(
        "$._embedded.genres[0].id",
        Matchers.equalTo( genre.id().intValue() )
      ) )
      .andExpect( jsonPath( "$._embedded.genres[0].name", Matchers.equalTo( genre.name() ) ) )
      .andExpect( jsonPath(
        "$._embedded.genres[1].description",
        Matchers.equalTo( genre.description() )
      ) )
      .andExpect( jsonPath( "$._links.genres.href", Matchers.notNullValue() ) );
  }

  @ParameterizedTest(name = "Accessing as {0}")
  @CsvSource({
    "USER", "MODERATOR", "ADMIN"
  })
  @DisplayName(
    "Should return status 200 and a genre when any type of user tries to get a genre by id")
  void findGenreById_AnyTypeOfUser_ShouldReturnOk(String role) throws Exception {
    GenreResponseDTO genre = CinemaFactory.createGenreResponseDTO();
    when( genreService.findById( genre.id() ) ).thenReturn( genre );

    mvc.perform( get(
        BASE_URL + "/{id}",
        genre.id()
      ).with( SecurityMockMvcRequestPostProcessors.user( role )
        .roles( role ) ) )
      .andExpect( status().isOk() )
      .andExpect( content().contentType( MediaTypes.HAL_JSON_VALUE ) )
      .andExpect( jsonPath( "$.id", Matchers.equalTo( genre.id().intValue() ) ) )
      .andExpect( jsonPath( "$.name", Matchers.equalTo( genre.name() ) ) )
      .andExpect( jsonPath( "$.description", Matchers.equalTo( genre.description() ) ) )
      .andExpect( jsonPath( "$._links.self.href", Matchers.notNullValue() ) );
  }

  @Test
  @DisplayName(
    "Should return status 200 and a genre when an anonymous user tries to get a genre by id")
  @WithAnonymousUser
  void findGenreById_AnonymousUser_ShouldReturnOk() throws Exception {
    GenreResponseDTO genre = CinemaFactory.createGenreResponseDTO();
    when( genreService.findById( genre.id() ) ).thenReturn( genre );

    mvc.perform( get( BASE_URL + "/{id}", genre.id() ) )
      .andExpect( status().isOk() )
      .andExpect( content().contentType( MediaTypes.HAL_JSON_VALUE ) )
      .andExpect( jsonPath( "$.id", Matchers.equalTo( genre.id().intValue() ) ) )
      .andExpect( jsonPath( "$.name", Matchers.equalTo( genre.name() ) ) )
      .andExpect( jsonPath( "$.description", Matchers.equalTo( genre.description() ) ) )
      .andExpect( jsonPath( "$._links.self.href", Matchers.notNullValue() ) );
  }
}
