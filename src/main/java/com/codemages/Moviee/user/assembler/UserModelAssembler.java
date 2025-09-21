package com.codemages.Moviee.user.assembler;

import com.codemages.Moviee.user.controller.v1.PrivateUserController;
import com.codemages.Moviee.user.dto.UserResponseDTO;
import lombok.NonNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.stream.StreamSupport;

@Component
public class UserModelAssembler implements
                                RepresentationModelAssembler<UserResponseDTO,
                                  EntityModel<UserResponseDTO>> {


  @NonNull
  @Override
  public CollectionModel<EntityModel<UserResponseDTO>> toCollectionModel(
    @NonNull
    Iterable<? extends UserResponseDTO> entities
  ) {
    Link selfLink = WebMvcLinkBuilder.linkTo( WebMvcLinkBuilder
        .methodOn( PrivateUserController.class ).getUsers() )
      .withRel( "users" );

    var models = StreamSupport.stream( entities.spliterator(), false )
      .map( this::toModel )
      .toList();

    return CollectionModel.of( models, selfLink );
  }

  @NonNull
  @Override
  public EntityModel<UserResponseDTO> toModel(
    UserResponseDTO dto
  ) {
    Link self = WebMvcLinkBuilder.linkTo( WebMvcLinkBuilder
        .methodOn( PrivateUserController.class ).getUserById( dto.id() ) )
      .withSelfRel();

    return EntityModel.of( dto, self );
  }
}
