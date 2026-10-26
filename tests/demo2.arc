//Conditions and logical expressions

func int main() {
    int marks = 85;
    int attendance = 90;

    if (marks >= 80 && attendance >= 75) {
        output(1);
    } else {
        output(0);
    }

    if (marks < 40 || attendance < 40) {
        output(0);
    } else {
        output(1);
    }

    send 0;
}