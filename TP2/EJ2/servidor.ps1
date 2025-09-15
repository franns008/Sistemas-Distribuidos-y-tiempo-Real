javac ServerEj3.java
for ($i = 1; $i -le 6; $i++) {
  java ServerEj3 5000$i >> resultados_servidor.txt &
}