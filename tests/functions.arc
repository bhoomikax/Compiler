func int add(int left, int right) {
    send left + right;
}

func int pair(int left, int right) {
    send left * 100 + right;
}

func int nested(int value) {
    send add(value, 3);
}

func decimal addDecimal(decimal value, int extra) {
    send value + extra;
}

func void main() {
    output(nested(4));
    output(pair(add(5, 6), add(30, 40)));
    output(addDecimal(2.5, 3));
}
