Scala for Java devs

HelloWorld

object HelloWorld {
  def main(args: Array[String]) {
    println("Hello, world!")
  }
}

The main method does not return a value (it is a procedure method). Therefore, it is not necessary to declare a return type.

What is less familiar to Java programmers is the object declaration containing the main method. Such a declaration introduces what is commonly known as a singleton object, that is a class with a single instance. The declaration above thus declares both a class called HelloWorld and an instance of that class, also called HelloWorld. This instance is created on demand, the first time it is used.

The astute reader might have noticed that the main method is not declared as static here. This is because static members (methods or fields) do not exist in Scala. Rather than defining static members, the Scala programmer declares these members in singleton objects.