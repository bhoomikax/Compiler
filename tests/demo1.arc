//Arithmetic and operator precedence
func int main() {
    int a = 10;
    int b = 3;

    int sum = a + b;
    int difference = a - b;
    int product = a * b;
    int remainder = a % b;
    int precedence = a + b * 2;

    output(sum);
    output(difference);
    output(product);
    output(remainder);
    output(precedence);

    send 0;
}