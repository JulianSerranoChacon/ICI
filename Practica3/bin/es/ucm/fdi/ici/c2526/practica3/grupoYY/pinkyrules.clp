;; DEFINITION OF DATA TYPES ;;

(deftemplate MSPACMAN
	(slot distanceToBlinky 			(type integer) (default -1))
	(slot distanceToPinky 			(type integer) (default -1))
	(slot distanceToInky 			(type integer) (default -1))
	(slot distanceToSue 			(type integer) (default -1))
	(slot closestIntersection 		(type integer) (default -1))
	(slot distanceToClosestPPill 	(type integer) (default -1))	
)

(deftemplate BLINKY
	(slot distanceToPacman 			(type integer)  (default -1))
	(slot distanceToPinky 			(type integer)  (default -1))
	(slot distanceToInky 			(type integer)  (default -1))
	(slot distanceToSue 			(type integer)  (default -1))
	(slot distanceToIntersection 	(type integer)  (default -1))
	(slot myShield		            (type SYMBOL) (default ""))
	(slot edibleTime				(type NUMBER) (default 0))
	(slot lairTime					(type NUMBER) (default 0))
)

(deftemplate INKY
	(slot distanceToPacman 			(type integer)  (default -1))
	(slot distanceToBlinky 			(type integer)  (default -1))
	(slot distanceToPinky 			(type integer)  (default -1))
	(slot distanceToSue 			(type integer)  (default -1))
	(slot distanceToIntersection 	(type integer)  (default -1))
	(slot myShield		            (type SYMBOL) (default ""))
	(slot edibleTime				(type NUMBER) (default 0))
	(slot lairTime					(type NUMBER) (default 0))
)

(deftemplate PINKY
	(slot distanceToPacman 			(type integer)  (default -1))
	(slot distanceToBlinky 			(type integer)  (default -1))
	(slot distanceToInky 			(type integer)  (default -1))
	(slot distanceToSue 			(type integer)  (default -1))
	(slot distanceToIntersection 	(type integer)  (default -1))
	(slot myShield		            (type SYMBOL) (default ""))
	(slot edibleTime				(type NUMBER) (default 0))
	(slot lairTime					(type NUMBER) (default 0))
)

(deftemplate SUE
	(slot distanceToPacman 			(type integer)  (default -1))
	(slot distanceToBlinky 			(type integer)  (default -1))
	(slot distanceToPinky 			(type integer)  (default -1))
	(slot distanceToInky 			(type integer)  (default -1))
	(slot distanceToIntersection 	(type integer)  (default -1))
	(slot myShield		            (type SYMBOL) (default ""))
	(slot edibleTime				(type NUMBER) (default 0))
	(slot lairTime					(type NUMBER) (default 0))
)

;; DEFINITION OF THE ACTION FACT (ALSO A DATA_TYPE lol) --> IS ALL IN THE PERSPECTIVE OF BLINKY, WE WILL ADAPT TO OTHER GHOSTS ;;

;; Basic action
(deftemplate ACTION
	; MANDATORY SLOTS ;
	(slot id			(type SYMBOL)			  ) 
	(slot info 			(type STRING) (default "")) 
	; OPTIONAL SLOTS ; 
	(slot extraGhost 	(type SYMBOL) (default NONE)) ; Extra slot for any action that requires a target ghost
	(slot intersection 	(type NUMBER) (default NONE)) ; Extra slot for any action that requires a target intersection
	(slot priority 		(type NUMBER) (default NONE))
) 

;; -------------------------------------------------------------------------------------------;;

;; RULES OF ALL GHOSTS ;;

;; DEDUCED INFORMATION ;;
;; LAIR ;;
(defrule PINKYinlair
	declare (salience 100)
	(PINKY (lairTime ?t))
	(test (> ?t 0))
	=>
	(assert 
		(ACTION 
			(id PINKYRandom) 
			(info "Random move")  
			(priority 100) 
		)
	)
)	

;; HUIDA ;;
(defrule PINKYpacmanFarAway
	declare (salience 21)
	(PINKY (distanceToPacman ?d) (lairTime ?t) (edibleTime ?e))
	(test (> ?e 0))
	(test (or (neq ?t 0) (> ?d (+ (/ ?e 2) 1))))  ;; far away if distance > (edibleTime/2 + 1)
		=>
	(assert
		(ACTION 
			(id PINKYOrbit)
			(info "PINKY far away and edible")
			(priority 21) 	
		)
	)
)

(defrule PINKYhayEscudero
	declare (salience 20)
	(PINKY (myShield ?g) (edibleTime ?e))
	(test (> ?e 0))
	=>
   	(assert
    	(ACTION 
        	(id PINKYRunToEscudero)
         	(info "PINKY going to escudero")
         	(extraGhost ?g)
         	(priority 20) 		
      	)
	)
)

(defrule PINKYpacmanNear
	declare (salience 19)	
	(PINKY (distanceToPacman ?d) (lairTime ?t) (edibleTime ?e))
	(test (> ?e 0))
	(test (or (== ?t 0) (< ?d 200)))  ;; near if distance < 200
		=>
	(assert
		(ACTION 
			(id PINKYrunsOptimal)
			(info "PINKY near and edible")
			(priority 19) 		
		)
	)
)

(defrule PINKYrunsAwayMSPACMANclosePPill
	declare (salience 18)
	(MSPACMAN (distanceToClosestPPill ?d)) 
	(test (<= ?d 30)) 
	=>  
	(assert 
		(ACTION 
			(id PINKYstatrRunning) 
			(info "MSPacMan cerca PPill") 
			(priority 18) 
		)
	)
)

;; PERSECUCION ;;
(defrule ShieldInky
	declare (salience 17)
	(INKY (myShield ? i))
	test (
		(eq ?i PINKY)
	)
	=>
	(assert 
		(ACTION 
			(id PINKYrunToTheEdible) 
			(info "me vuelvo escudero") 
			(extraGhost INKY) 
			(priority 17)
		)
	)
)

(defrule ShieldBlinky
	declare (salience 17)
	(BLINKY (myShield ? b))
	test (
		(eq ?b PINKY)
	)
	=>
	(assert 
		(ACTION 
			(id PINKYrunToTheEdible) 
			(info "me vuelvo escudero") 
			(extraGhost BLINKY) 
			(priority 17)
		)
	)
)

(defrule ShieldSue
	declare (salience 17)
	(SUE (myShield ? s))
	test (
		(eq ?s PINKY)
	)
	=>
	(assert 
		(ACTION 
			(id PINKYrunToTheEdible) 
			(info "me vuelvo escudero") 
			(extraGhost SUE) 
			(priority 17)
		)
	)
)

(defrule PINKYNearestToMsPacman
	declare (salience 15)
	(BLINKY (distanceToPacman ?blinkyDistance)) 	; Hecho para la distancia de Blinky
	(PINKY 	(distanceToPacman ?pinkyDistance))   	; Hecho para la distancia de Pinky
	(INKY  	(distanceToPacman ?inkyDistance))     	; Hecho para la distancia de Inky
	(SUE	(distanceToPacman ?sueDistance))       	; Hecho para la distancia de Sue
	(test (<= ?pinkyDistance ?blinkyDistance))
	(test (<= ?pinkyDistance ?inkyDistance))
	(test (<= ?pinkyDistance ?sueDistance))
	=> 
	(assert 
		(ACTION 
			(id PINKYHunter1) 
			(info "Soy cazador1")  
			(priority 15) 
		)
	)
)

(defrule PINKYSecondNearestToMsPacman
	declare (salience 14)
  	(BLINKY (distanceToPacman ?blinkyDistance)) 	; Hecho para la distancia de Blinky
	(PINKY 	(distanceToPacman ?pinkyDistance))   	; Hecho para la distancia de Pinky
	(INKY  	(distanceToPacman ?inkyDistance))     	; Hecho para la distancia de Inky
	(SUE	(distanceToPacman ?sueDistance))       	; Hecho para la distancia de Sue
	(test
		(or
			(and (> ?pinkyDistance ?blinkyDistance)
				(<= ?pinkyDistance ?inkyDistance)
				(<= ?pinkyDistance ?sueDistance)
				(bind ?closestGhost PINKY))
			(and (> ?pinkyDistance ?blinkyDistance)
				(<= ?pinkyDistance ?pinkyDistance)
				(<= ?pinkyDistance ?sueDistance)
				(bind ?closestGhost INKY))
			(and (> ?pinkyDistance ?sueDistance)
				(<= ?pinkyDistance ?blinkyDistance)
				(<= ?pinkyDistance ?inkyDistance)
				(bind ?closestGhost SUE))
    	)
  	)
  =>
  	(assert 
  		(ACTION 
			(id PINKYHunter2) 
			(info "Soy Hunter2") 
			(extraGhost closestGhost) 
			(priority 14)
		)
	)
)

(defrule PINKYNearestToIntersection
	declare (salience 13)
	(MSPACMAN 	(closestIntersection 	?closestintersection))
	(BLINKY 	(distanceToIntersection ?blinkyDistance)) 	; Hecho para la distancia de Blinky
	(PINKY 		(distanceToIntersection ?pinkyDistance))   	; Hecho para la distancia de Pinky
	(INKY  		(distanceToIntersection ?inkyDistance))     	; Hecho para la distancia de Inky
	(SUE		(distanceToIntersection ?sueDistance))       	; Hecho para la distancia de Sue
	(test (<= ?pinkyDistance ?blinkyDistance))
	(test (<= ?pinkyDistance ?inkyDistance))
	(test (<= ?pinkyDistance ?sueDistance))
	=> 
	(assert 
		(ACTION 
			(id PINKYJailer) 
			(info "Soy Jailer") 
			(intersection ?closestintersection) 
			(priority 13) 
		)
	)
)