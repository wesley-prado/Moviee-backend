package com.codemages.Moviee.cinema.movie.constant;

import lombok.Getter;
import org.springframework.hateoas.server.core.Relation;

@Relation(value = "genre", collectionRelation = "genres")
@Getter
public enum GenreEnum {
  ACTION(
    "Action",
    "Action is a high-stakes genre focused on physical conflict, stunts, and energetic sequences." +
      " Protagonists often overcome overwhelming odds through their physical prowess and skill, " +
      "leading to thrilling and fast-paced narratives."
  ), ADVENTURE(
    "Adventure",
    "This genre centers on a protagonist's journey to exotic locations. The plot is driven by a " +
      "quest or pursuit of the unknown, with the hero facing various dangers and obstacles that " +
      "lead to personal growth and transformation."
  ), ANIMATION(
    "Animation",
    "Animation is a film genre where the illusion of movement is created from a series of still " +
      "images. Known for its creativity and imagination, it brings characters and objects to " +
      "life, spanning a wide range of stories for both children and adults."
  ), COMEDY(
    "Comedy",
    "Comedy is a genre of entertainment designed to provoke laughter and amusement. It often " +
      "places relatable characters in humorous or absurd situations, using wit and funny " +
      "situations to provide social commentary or simply to entertain, and typically ends happily."
  ), DRAMA(
    "Drama",
    "Drama is a serious genre focused on the emotional and relational development of characters. " +
      "It explores profound themes through compelling conflicts, aiming to evoke strong emotions " +
      "in the audience and providing a realistic look at the human condition.\n"
  ), HISTORICAL(
    "Historical",
    "Set in a specific past time period, this genre blends factual research with fictional " +
      "elements to bring a historical era to life. It often focuses on real people, events, and " +
      "places, telling stories that reflect the past."
  ), HORROR(
    "Horror",
    "The horror genre aims to evoke feelings of fear, dread, and anxiety. Using suspense and a " +
      "chilling atmosphere, it explores themes of death, the supernatural, and the unknown to tap" +
      " into the audience's primal fears."
  ), TERROR(
    "Terror",
    "Terror focuses on the psychological suspense and dread that precedes a horrifying event. It " +
      "emphasizes anticipation and the feeling of what is about to happen, rather than the " +
      "explicit visuals and gore of horror itself."
  ), WESTERN(
    "Western",
    "Typically set in the American frontier of the late 19th century, this genre explores the " +
      "conflict between wilderness and civilization. Stories often feature cowboys or " +
      "gunslingers, with common themes of justice, revenge, and the law."
  ), SCI_FI(
    "Sci-Fi",
    "This genre incorporates real or imaginary science and technology into its plot. It explores " +
      "futuristic concepts like space travel and artificial intelligence, often speculating on " +
      "their impact on humanity and society."
  ), ROMANCE(
    "Romance",
    "The romance genre centers on the development of a romantic relationship between two " +
      "protagonists. A key characteristic is a positive and emotionally satisfying conclusion, " +
      "often referred to as a \"happily-ever-after.\""
  );

  private final String name;
  private final String description;

  GenreEnum(String name, String description) {
    this.name = name;
    this.description = description;
  }
}
