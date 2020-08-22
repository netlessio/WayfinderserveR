def sum(a: Int, b:Int):Int =  a + b
def fib(f: (Int, Int) => Int)(a: Int): Int = {
    if (a <= 0)  0
    else if (a == 1) 1
    else f(fib(f)( a - 1), fib(f)(a - 2));
}

def fib2(f: (Int, Int) => Int)(a: Int): Int = a match {
    c