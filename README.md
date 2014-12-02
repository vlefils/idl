idl
===

Importer le projet dans eclipse (projet maven, il devrait récupérer les dépendances sans soucis)

Récupérer les traces éclipse (voir moodle) et les mettres dans le dossier /src/main/ressources/jsonfiles

Lancer le main :
  - la variable verbose indique si l'ont liste le contenu des bucket
  - la variable nbfiles indique le nombre de traces à prendre en compte (500 pour un test rapide)
  
  dans la main on peut également choisir d'utiliser clusteringSimple ou clusteringMemoire 
    selon si l'on veut ou non stocker en mémoire les données déja calculée (cf compte rendu)
    
  Même si les performances de l'algo des Centres sont moindres (cf compte rendu) 
    on peut choisir d'appeler clustering.algoCentres() ou clustering.algoVoisins()
    
  La main est livrée avec affichage détaillé et mesure de temps exécution des différentes parties (init et algo)
