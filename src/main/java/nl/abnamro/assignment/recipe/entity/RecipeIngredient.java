package nl.abnamro.assignment.recipe.entity;

import javax.persistence.EmbeddedId;

//@Entity
//@Data
//@Table(name = "recipe-ingredient")
public class RecipeIngredient {

   @EmbeddedId
   private RecipeIngredientId id;

   /*
   @ManyToOne(fetch = FetchType.LAZY)
   @MapsId("postId")
   private RecipeEntity recipe;

   @ManyToOne(fetch = FetchType.LAZY)
   @MapsId("tagId")
   private IngredientEntity tag;
   */

}
