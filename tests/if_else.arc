func void main() {
    if (yes) {
        output("then branch");
    } else {
        output("unexpected");
    }

    if (no) {
        output("unexpected");
    } else {
        output("else branch");
    }
}
