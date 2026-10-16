func void main() {
    int total = 0;
    each (int index = 0; index < 8; index = index + 1) {
        if (index == 2) {
            skip;
        }
        if (index == 5) {
            break;
        }
        total = total + index;
    }
    output(total);
}
