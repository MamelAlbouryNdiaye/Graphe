package main.java;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import java.util.*;
import java.util.stream.Stream;
/**
 * @author Ndiaye Mamel Alboury
 * Cette classe écrite en java nous permet grace aux méthodes utilises de trouver le plus court chemin entre
 * un noeud source du graphe et entre chaque paire de noeuds du graphe
 */

public class PlusCourtChemin {
    protected String styleSheet = "node {" + "fill-color: black;" + "}" + "node.marked {" + " fill-color: red;" + "}";

    /**
     * Constructeur de notre classe
     *
     * @param gr Graph
     */
    public PlusCourtChemin(Graph gr) {
        gr.setAttribute("ui.stylesheet", styleSheet);
    }


    /**
     * Affiche l'identifiant de chaque noeud sur le graphe en cas de display
     *
     * @param g Graph
     */
    void markGraph(Graph g) {

        //Affiche l'id de chaque noeud lorsqu'on affiche le graphe
        for (Node node : g) {
            node.setAttribute("ui.label", node.getId());
            node.setAttribute("ui.class", "marked");
        }
        //Affiche le poid de chaque arête lorsqu'on affiche le graphe
        g.edges().forEach(e -> e.setAttribute("ui.label", "" + e.getAttribute("weight")));
    }

    /**
     * cette méthode permet de trouver le plus court chemin entre le noeud source et tous les autres noeuds du graphe
     *
     * @param g Graph
     * @param s Node
     * @return ArrayList
     */

    public ArrayList lesPlusCourtsCheminsDijkstra(Graph g, Node s) {
        ArrayList chemins = new ArrayList();
        g.nodes().filter(x -> x != s).forEach(n
                -> {
            if (!plusCourtCheminDijkstra(g, s, n).empty()) {
                //appel a la méthode plusCourtCheminDijkstra pour pouvoir trouver le plus court chemin entre le noeud source et tous les autres noeuds du graphe (n le noeud courant)
                chemins.add("\n" + plusCourtCheminDijkstra(g, s, n));

            }
        });
        return chemins;
    }


    /**
     * Donne le plus court chemin entre les sommets a et b du Graphe g en utilisant l'algorithme de Dijkstra
     *
     * @param g Graph
     * @param s Node
     * @param t Node
     * @return Path
     */
    public Path plusCourtCheminDijkstra(Graph g, Node s, Node t) {
        //Le poid des arêtes est stockée dans l'attribut "length"
        Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, null, "weight");

        //Compile tous les plus courts chemins entre a et b
        dijkstra.init(g);
        dijkstra.setSource(s);
        dijkstra.compute();

        return dijkstra.getPath(t);
    }


    /**
     * cette méthode permet de trouver le plus court chemin entre le noeud source et tous les autres noeuds du graphe
     *
     * @param g Graph
     * @param s Node
     * @return ArrayList
     */
    public ArrayList lesPlusCourtsCheminsDeMonAlgo(Graph g, Node s) {
        ArrayList chemins = new ArrayList();

        // parcourt de tous les noeuds du graphes en filtrant sans noeud source
        g.nodes().filter(x -> x != s).forEach(n
                -> {
            if (!plusCourtCheminDeMonAlgo(g, s, n).isEmpty()) {
                //appel a la méthode chemin pour pouvoir trouver le plus court chemin entre le noeud source et tous les autres noeuds du graphe (n le noeud courant)
                chemins.add("\n" + plusCourtCheminDeMonAlgo(g, s, n));
            }
        });

        return chemins;
    }


    /**
     * cette méthode nous affiche le plus court chemin entre le noeud source s  et le noeud d'arriver t
     *
     * @param g Graph
     * @param s Node
     * @param t Node
     * @return ArrayList
     */
    public ArrayList plusCourtCheminDeMonAlgo(Graph g, Node s, Node t) {
        // on appel notre methode dijkstra qui calcul le plus court chemin entre le noeud source s et tous les autres noeuds du graphe g
        dijkstra(g, s);

        //création d'une arrayList pour stocker les noeuds de notre plus court chemin dedans.
        ArrayList<Node> chemin = new ArrayList();


        //vérifier si notre noeud d'arriver contient un parent s'il ne contient pas de parent alors il n'existe pas de chemin entre le noeud s et t
        if (t.getAttribute("parent") != null) {
            // création d'une variable parent qui va contenir au début notre noeud d'arriver
            Node parent = t;
            //création d'une variable n qui va contenir au début notre noeud d'arriver t dans le but
            // de garder la valeur de t a la fin car j'ai besoin de faire des mise a jour sur elle
            Node n = t;
            //ajouter le noeud d'arriver a la liste pour qu'on puisse voir tous noeuds de notre plus court chemin
            chemin.add(parent);


            // une boucle tant que le parent n'est pas la source alors on cherche le parent du noeud courant
            while (parent != s) {
                // parent devient le parent du noeud d'arriver t
                parent = (Node) n.getAttribute("parent");

                // n recoit le parent courant
                n = parent;
                //Ajout du parent dans la liste
                chemin.add(parent);
            }
            // pour inverser notre liste car lorqu'on stock les noeuds parent dans la liste ils seront ordonnés du noeud t vers s, alors pour obtenir une liste
            //ordonnée du noeud source s vers t on utilise Collections.reverse sur notre liste
            Collections.reverse(chemin);
        }
        return chemin;
    }


    /**
     * cette méthode donne les plus courts chemins d'un noeud source vers tous les autres noeuds du graphe
     *
     * @param g Graph
     * @param s Node
     */
    public void dijkstra(Graph g, Node s) {
// initialise tous les noeuds du graphe a l'infini et le parent de chaque noeud à null
        g.nodes().forEach(n -> {
            n.setAttribute("distance", (int) Double.POSITIVE_INFINITY);
            n.setAttribute("parent", (Object) null);
        });


// associe la valeur 0 à la distance de notre noeud Source
        s.setAttribute("distance", 0);
        // Création d'une file priority qui permet de stocker les noeuds selon la priorité (qui contient la distance minimale!)
        PriorityQueue<Node> liste = new PriorityQueue<>(11, Comparator.comparingInt(this::distance));
        // Ajout du noeud source dans la file
        liste.add(s);


// boucle sur la file , tant que la file n'est pas vide on parcours notre file
        while (!liste.isEmpty()) {

            // récupération du noeud qui contient la distance minimale
            // remove : elle le récupére et le supprime de la file
            Node u = liste.remove();

            //System.out.println("noeud extrait  -----: "+ u + " sa distance =  "+ u.getAttribute("distance")+" -----" );

            // récupération des arêtes sortantes du noeud u(le voisin qui contient la distance minimale)  dans un stream
            Stream<Edge> aretes = u.leavingEdges();
            // création d'un iterator sur le stream qui contient les aretes sortantes du noeud u dans le but de parcourir
            // toutes les aretes et récupérer le noeud de direction
            Iterator<Edge> i = aretes.iterator();
            // boucle sur l'iterator
            while (i.hasNext()) {
                //récupére une arête sortante de u  dans e
                Edge e = i.next();
                // récupére le noeud de direction (le noeud qui va avoir e comme arête entrante) u-->v
                Node v = e.getTargetNode();

                // création d'une variable w qui contient la somme de la distance du noeud u et le poid de son arête sortante courante dans le but de pouvoir faire la comparaison
                int p =  (distance(u) + (int) (e.getAttribute("weight")));
                // vérifier si la distance de notre noeud d'arriver v (un voisin de u ) est supérieur a w (la somme de la distance du noeud u et le poid de son arête sortante)
                if (distance(v) > p) {
                    // mise à jour de la distance du noeud v qui va prendre la valeur de  w (la somme de la distance du noeud u et le poid de son arête sortante)
                    v.setAttribute("distance", p);
                    // on associe u comme parent à v
                    v.setAttribute("parent", u);
                    //on vérifie si notre file contient le noeud v (si on est deja passé par ce noeud) si c'est le cas alors on l'ajoute pas une autre fois dans la file
                    if (!liste.contains(v)) {
                        liste.add(v);
                    }

                }
            }

        }
    }


    /**
     * Affiche la distance d'un noeud passer en paramétre
     *
     * @param n Node
     * @return int
     */
    public int distance(Node n) {
        return (int) n.getAttribute("distance");
    }

    /**
     *  méthode principale
     */

    public static void main(String[] args) {
        //Noeud source
        int source;
        //nombre des noeuds du graphe
        int nbNoeuds;
        //degré moyen
        int degreeMoyen;
        //reponse pour l'affichage
        String reponse2;

        //pour la saisie en terminale (Entrée clavier)
        Scanner clavier = new Scanner(System.in);
        System.out.print("Saisir le nombre de noeuds du Graphe : ");
        nbNoeuds = clavier.nextInt();

        while (nbNoeuds <= 0) {
            System.out.print("il faut saisir un nombre positif Non Null !!  ");
            nbNoeuds = clavier.nextInt();
        }

        System.out.print("Saisir le Degré moyen du graphe : ");
        degreeMoyen = clavier.nextInt();
        while (degreeMoyen < 0) {
            System.out.print("il faut saisir un degré Positif  !!  ");
            degreeMoyen = clavier.nextInt();
        }

        System.out.print("Saisir le noeud source souhaité : ");
        source = clavier.nextInt();
        while (source < 0 || source > nbNoeuds) {
            System.out.print("il faut saisir un noeud compris dans le graphe !!  ");
            source = clavier.nextInt();
        }


        //configurer l'apparence du graphe, j'ai choisis le rendu swing (faut la définir avant d'appeler graphe.display())
        System.setProperty("org.graphstream.ui", "swing");
        //instancier un random
        Random r = new Random();
        // instancie un graphe g en faisant l'implémentation de graphe SingleGraph
        Graph g = new SingleGraph("RandomGraph");
        //instancie un objet de ma classe Graph1
        PlusCourtChemin graph = new PlusCourtChemin(g);
        // instancie un générateur gen qui nous permettre de  crée des graphiques aléatoires de toute taille
        Generator gen = new RandomGenerator(degreeMoyen, false, true);
        gen.addSink(g);
        //ajoute un noeud unique dans graphe
        gen.begin();
        //ajoute un nouveau noeud
        for (int i = 0; i < nbNoeuds; i++)
            gen.nextEvents();
        gen.end();

        //on associe pour chaque arête du graphe un poid en utilisant random
        g.edges().forEach(e
                -> e.setAttribute("weight", r.nextInt(50))
        );

       // g.display();

        System.out.println("LES PLUS COURTS CHEMINS  DE MON ALGO SONT : ");
        graph.markGraph(g);
        // création d'une variable startTime pour déclencher le temps et commencer le calcul
        double startTime = System.currentTimeMillis();
        System.out.println("Les plus courts chemins entre le noeud " + g.getNode(source) + "  et tous les autres noeuds du Graphe sont \n " + graph.lesPlusCourtsCheminsDeMonAlgo(g, g.getNode(source)));
        // on arrete d'incrémenter le temps une fois qu'on a retourner notre plus court chemin ent a et b
        double stopTime = System.currentTimeMillis();
        // affiche la distance de notre noeud d'arriver
        //graph.distance(g.getNode(99));

        // calcul le temps d'exécution de mon algo implémenter
        double tempsExecutionMonAlgo = (stopTime - startTime) / 1000;

        System.out.println("----------------------------------------------------------------------------------------------------------------------");

        System.out.println(" --------------- LES PLUS COURTS CHEMINS DE Dijkstra SONT : ------------------------");
        // création d'une variable startTim pour déclencher le temps et commencer le calcul
        double startTim = System.currentTimeMillis();
        System.out.println("Les plus courts chemins entre le noeud " + g.getNode(source) + "  et tous les autres noeuds du Graphe sont \n " + graph.lesPlusCourtsCheminsDijkstra(g, g.getNode(source)));
        // on arrete d'incrémenter le temps une fois qu'on a retourner notre plus court chemin ent a et b
        double stopTim = System.currentTimeMillis();
        // calcul le temps d'exécution de l'algo déja défini
        double tempsExecutionExsistant = (stopTim - startTim) / 1000;
        System.out.println("Temps d'execution de l'algorithme qui existait dans GraphStream => " + tempsExecutionExsistant + " s");
        System.out.println("Temps d'execution de l'algorithme de mon algo => " + tempsExecutionMonAlgo + " s");

        System.out.println();

        System.out.print("Souhaitez-vous maintenant trouver le plus court chemin entre le noeud source et un autre noeud du graphe  \n Si c'est le cas saisissez oui  ");
        Scanner clavie = new Scanner(System.in);
        reponse2 = clavie.nextLine();

        if (reponse2.equals("oui")) {
            int t;
            System.out.print("Saisir le noeud d'arriver : ");
            t = clavier.nextInt();
            while (t < 0 || t > nbNoeuds) {
                System.out.print("Saisir le noeud d'arriver : ");
                t = clavier.nextInt();
            }
            graph.markGraph(g);
            System.out.println("------------------------------ Dans Mon Algo  ------------------------");
            double start = System.currentTimeMillis();
            System.out.print("Le plus court chemin entre le noeud " + g.getNode(source) + "  et le noeud  " + g.getNode(t) + " selon mon Algo implémenter est  :" + graph.plusCourtCheminDeMonAlgo(g, g.getNode(source), g.getNode(t)) + "\n");
            double stop = System.currentTimeMillis();
            double tempsExecutionMonAlg = (stop - start) / 1000;

            System.out.println("------------------------------ Dans L'algo de GraphStream ------------------------");

            double startT = System.currentTimeMillis();
            System.out.println("Le plus court chemin entre le noeud " + g.getNode(source) + "  et le noeud  " + g.getNode(t) + " selon l'algo de graphStream  :" + graph.plusCourtCheminDijkstra(g, g.getNode(source), g.getNode(t)));
            // on arrete d'incrémenter le temps une fois qu'on a retourner notre plus court chemin ent a et b
            double stopT = System.currentTimeMillis();
            // calcul le temps d'exécution de l'algo déja défini
            double tempsExecutionExsistan = (stopT - startT) / 1000;
            System.out.println("------------------------------ Temps d'éxécution  ------------------------");
            System.out.println("Temps d'execution de l'algorithme qui existait dans GraphStream => " + tempsExecutionExsistan + " s");
            System.out.println("Temps d'execution de l'algorithme de mon algo => " + tempsExecutionMonAlg + " s");


        }

    }

}
