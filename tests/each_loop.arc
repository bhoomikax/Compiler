func void main() {
    int total = 0;
    each (int index = 0; index < 5; index = index + 1) {
        total = total + index;
    }
    output(total);
}
