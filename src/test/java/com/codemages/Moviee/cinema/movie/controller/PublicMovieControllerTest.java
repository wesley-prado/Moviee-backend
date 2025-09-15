package com.codemages.Moviee.cinema.movie.controller;

import com.codemages.Moviee.cinema.movie.MovieService;
import com.codemages.Moviee.cinema.movie.assembler.MovieModelAssembler;
import com.codemages.Moviee.core.constant.ControllerConstant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.codemages.Moviee.auth.security.factory.MovieFactory.createMovieResponseDTO;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PublicMovieController.class)
@Import(MovieModelAssembler.class)
@AutoConfigureMockMvc(addFilters = false)
public class PublicMovieControllerTest {
  @MockitoBean
  private MovieService movieService;

  @Autowired
  private MockMvc mvc;

  @Test
  @DisplayName("Deve retornar status 200 quando um usuário não autenticado acessar a API GET " +
    "/movies")
  @WithAnonymousUser
  void getAllMovies_AnonymousUser_ShouldReturnOk() throws Exception {
    when( movieService.findAll() ).thenReturn( List.of(
      createMovieResponseDTO(),
      createMovieResponseDTO(),
      createMovieResponseDTO()
    ) );

    mvc.perform( get( ControllerConstant.PUBLIC_API_BASE + "/v1/movies" )
        .secure( true ) )
      .andExpect( status().isOk() )
      .andExpect( content().contentType( MediaTypes.HAL_JSON_VALUE ) )
      .andExpect( jsonPath( "$._embedded.movies", hasSize( 3 ) ) );
  }
}
