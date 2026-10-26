func int add(int left, int right) {
    send left + right;
}

func int multiply(int left, int right) {
    send left * right;
}

func decimal half(int value) {
    send value / 2.0;
}

func string label() {
    send "nested";
}

func void main() {
    int sum = add(10, 20);
    int result = multiply(add(2, 3), 4);
    int plain = 7;
    int chained = add(multiply(sum, 2), add(result, plain));
    decimal fraction = half(sum);
    string text = label();

    int total = 0;
    each (int index = add(0, 1); index <= 3; index = index + 1) {
        int step = multiply(index, add(index, 1));
        total = total + step;
    }

    if (sum == 30) {
        int inner = add(sum, result);
        output(inner);
    }

    output(sum);
    output(result);
    output(plain);
    output(chained);
    output(fraction);
    output(text);
    output(total);
}
