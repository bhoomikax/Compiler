func int tooMany(int first, int second, int third, int fourth, int fifth) {
    send first + second + third + fourth + fifth;
}

func void main() {
    output(tooMany(1, 2, 3, 4, 5));
}
