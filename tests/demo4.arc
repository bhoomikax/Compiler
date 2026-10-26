User-defined functions and nested calls

func int add(int a, int b) {
    send a + b;
}

func int multiply(int a, int b) {
    send a * b;
}

func int main() {
    int sum = add(10, 20);
    int result = multiply(add(2, 3), 4);

    output(sum);
    output(result);

    send 0;
}