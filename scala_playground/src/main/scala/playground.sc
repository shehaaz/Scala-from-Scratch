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
//-------------//

/**
  * Option Collections
  *
  * Some developers see Option as a safe replacement for null values,
  * notifying users that the value may be missing and
  * reducing the likelihood that its use will trigger a Null PointerException.
  * Others see it as a safer way to build chains of operations,
  * ensuring that only valid values will persist for the duration of the chain.
  *
  * The Option type is itself unimplemented but relies on two subtypes for the implementation:
  * Some, a type-parameterized collection of one element; and
  * None, an empty collection. The None type has no type parameters because it never contains contents.
  */

var indeed: String = "Indeed"
var optionIndeed = Option(indeed)
indeed = null
optionIndeed = Option(indeed)

def divide(amt: Double, divisor: Double) : Option[Double] =
{
  if(divisor == 0) None
  else Option(amt/divisor)
}
//divide: divide[](val amt: Double,val divisor: Double) => Option[Double]

val legit = divide(5,2)
//legit: Option[Double] = Some(2.5)
val illegal = divide(3,0)
//illegal: Option[Double] = None

val value_legit:Any = legit.getOrElse("You Divided by Zero Yo");
//value_legit: Any = 2.5
val value_illegal:Any = illegal.getOrElse("You Divided by Zero Yo");
//value_illegal: Any = You Divided by Zero Yo

val odds = List(1, 3, 5)
//odds: List[Int] = List(1, 3, 5)
/**
  * headOption, which returns the head element wrapped in an Option,
  * ensuring that it will work even on empty lists.
  */
val firstOdd = odds.headOption
//firstOdd: Option[Int] = Some(1)
val first:Any = firstOdd.getOrElse("BAD")
val evens = odds filter (_ % 2 == 0)
//evens: List[Int] = List()
val firstEven = evens.headOption
//firstEven: Option[Int] = None
val multipleOfFive = odds filter (_ % 5 == 0)
//multipleOfFive: List[Int] = List(5)

val words = List("risible", "scavenger", "GIST", "passenger", "ranger", "ranker")
//Predicate function: Functions that return a boolean.
val uppercase:Option[String] = words find (word => (word == word.toUpperCase))
//uppercase: Option[String] = Some(GIST)

val o:Object = uppercase.getOrElse(new Animal())
//o: Object = GIST
val a:Any = uppercase.getOrElse(5)
//a: Any = GIST
val s:String = uppercase.getOrElse("EMPTY STRING")
//s: String = GIST


val lowercase = words filter (word => (word == word.toLowerCase))
//lowercase: List[String] = List(risible, scavenger, passenger, ranger, ranker)
val filtered = lowercase filter (_ endsWith "er") map (_.toUpperCase)
//filtered: List[String] = List(SCAVENGER, PASSENGER, RANGER, RANKER)
val sayBravo = filtered filter (_.size == 6) map (_.concat("_BRAVO"))
//sayBravo: List[String] = List(RANGER_BRAVO, RANKER_BRAVO)
filtered.foreach((word: String) => print(word + " "))
//SCAVENGER PASSENGER RANGER RANKER res3: Unit = ()




class Stack[T] {
  var elems: List[T] = Nil
  def push(x: T) { elems = x :: elems }
  def top: T = elems.head
  def pop() { elems = elems.tail }
}

val stack = new Stack[Char]
stack.push(1)
stack.push('a')
println(stack.top)
stack.pop()
println(stack.top)
stack.pop()

