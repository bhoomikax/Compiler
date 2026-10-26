//Loops, break, and skip

func int main() {
    int i = 0;

    loop (i < 10) {
        i = i + 1;

        if (i == 3) {
            skip;
        }

        if (i == 7) {
            break;
        }

        output(i);
    }

    send 0;
}