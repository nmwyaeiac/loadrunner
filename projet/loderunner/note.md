la classe entiter permet d heriter toutes les autres elle contient entre autre des cordonnee 
et aussi une methode colide qui renvoie un boolean si 2 entiter sont au meme coordonnee

un wall qui est une classe abtraite qui va faire heriter 2 autre mur 
cassable et incassable, l
es incasable pour delimiter la map
les casssable permette de coincer des enemies 

une classe abstraite charactere qui va gerer les entiter avc des mouvement 
on sauvegarde la direction pour pouvoir la reutiliser dans les player pour casser les bloc en diagonal 
on a egalement un butin pour pouvoir stocker le no;bre d or qu on recupere 
une capaciter pour limiter celle des enemies a 1 
une methode qui verifie si le mouvmeent est possible si l objet n es pas bloquant 
et une methode pour prendre de l or et la marquer comme prise si c est le cas 

les enemies non pas encore ete implementer mais il doivent etre piloter par une ia et avoir une capaciter de 1 

la methode game object definie  si un objet est solid et si il est collectible au moyen de boolean 

un gold est collectible et non solid on lui met une nethode collectible qui va versifier 
si il est collecter si c est le cas il est plus afficher et ne petmet plus de donner de l or 

les methodes ladder monkeys bars permet de franchir la map en hauter et en longueur respectivment 


