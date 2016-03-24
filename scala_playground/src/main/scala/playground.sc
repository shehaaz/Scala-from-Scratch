trait Philosophical {
  def philosophize(): Unit = {
    println("I consume memory, therefore I am!")
  }
}

class Frog extends Philosophical {
  override def toString = "green"
}

val frog = new Frog
frog.philosophize()
//The type of phil is Philosophical, a trait
val phil: Philosophical = frog
phil.philosophize()

/**
  * You can "extends" and "with" a trait, but you can only "extends" a class.
  * You can add multiple "with" clauses to mix in multiple "traits"
  */
class Animal
trait HasLegs

class Frog_Animal extends Animal with Philosophical with HasLegs {
  override def toString = "green"
  override def philosophize(): Unit = {
    println("It ain't easy being " + toString + "!")
  }
}

val frogAnimal : Frog_Animal = new Frog_Animal
frogAnimal.philosophize()


/**
  * Initializing abstract vals
  */


