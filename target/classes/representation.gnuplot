set terminal wxt
set terminal pngcairo enhanced font 'Verdana, 10'
set encoding utf8
set output "Comparaison.png"
set xrange [0:100000]
set yrange [0:5000]
set xlabel "Nombre de noeuds"
set ylabel "Temps en ms"


plot "djikstraNaive" t "Dijkistra Naive" with linesp lt 2 pt 2, "djikstraGS" t "Dijkistra GS" with linesp lt 3 pt 3
