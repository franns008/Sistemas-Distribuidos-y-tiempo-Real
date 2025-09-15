javac Client.java
for ($i = 1; $i -le 6; $i++) {
    java Client 198.51.100.1 5000$i >> resultados.txt
}