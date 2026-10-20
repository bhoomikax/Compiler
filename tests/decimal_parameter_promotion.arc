func decimal half(decimal value) {
    send value / 2;
}

func decimal scale(int count, decimal factor, decimal offset, int extra) {
    send count * factor + offset + extra;
}

func int first(int value, decimal unused) {
    send value;
}

func void main() {
    output(half(3));
    output(half(3.0));
    int whole = 9;
    output(half(whole));
    output(scale(2, 1.5, 4, 1));
    output(first(7, 2));
}
