;; DEFINITION OF DATA TYPES ;;

(deftemplate MSPACMAN
	(slot distanceToBlinky 			(type float) (default -1))
	(slot distanceToPinky 			(type float) (default -1))
	(slot distanceToInky 			(type float) (default -1))
	(slot distanceToSue 			(type float) (default -1))
	(slot closestIntersection 		(type integer) (default -1))
	(slot distanceToClosestPPill 	(type float) (default -1))	
)

(deftemplate BLINKY
	(slot distanceToPacman 			(type float)  (default -1))
	(slot distanceToPinky 			(type float)  (default -1))
	(slot distanceToInky 			(type float)  (default -1))
	(slot distanceToSue 			(type float)  (default -1))
	(slot distanceToIntersection 	(type float)  (default -1))
	(slot myShield		            (type SYMBOL) (default ""))
	(slot edibleTime				(type NUMBER) (default 0))
	(slot lairTime					(type NUMBER) (default 0))
)

(deftemplate INKY
	(slot distanceToPacman 			(type float)  (default -1))
	(slot distanceToBlinky 			(type float)  (default -1))
	(slot distanceToPinky 			(type float)  (default -1))
	(slot distanceToSue 			(type float)  (default -1))
	(slot distanceToIntersection 	(type float)  (default -1))
	(slot myShield		            (type SYMBOL) (default ""))
	(slot edibleTime				(type NUMBER) (default 0))
	(slot lairTime					(type NUMBER) (default 0))
)

(deftemplate PINKY
	(slot distanceToPacman 			(type float)  (default -1))
	(slot distanceToBlinky 			(type float)  (default -1))
	(slot distanceToInky 			(type float)  (default -1))
	(slot distanceToSue 			(type float)  (default -1))
	(slot distanceToIntersection 	(type float)  (default -1))
	(slot myShield		            (type SYMBOL) (default ""))
	(slot edibleTime				(type NUMBER) (default 0))
	(slot lairTime					(type NUMBER) (default 0))
)

(deftemplate SUE
	(slot distanceToPacman 			(type float)  (default -1))
	(slot distanceToBlinky 			(type float)  (default -1))
	(slot distanceToPinky 			(type float)  (default -1))
	(slot distanceToInky 			(type float)  (default -1))
	(slot distanceToIntersection 	(type float)  (default -1))
	(slot myShield		            (type SYMBOL) (default ""))
	(slot edibleTime				(type NUMBER) (default 0))
	(slot lairTime					(type NUMBER) (default 0))
)

(deftemplate ROLES
	(slot hunter1					(type SYMBOL) (default NONE))
	(slot hunter2					(type SYMBOL) (default NONE))
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

;; DEDUCED INFORMATION ;;
(defrule init-roles
	(declare (salience 1000))
	(not (ROLES))
	=>
	(assert (ROLES (hunter1 NONE) (hunter2 NONE)))
)

(defrule PINKYsavesBLINKY
	(declare (salience 120))
	(PINKY 	(edibleTime ?be) (lairTime ?bl) (distanceToBlinky ?dis))
	?blinkyFact <- (BLINKY (edibleTime ?ie) (lairTime ?il) (myShield ?shield))
	(MSPACMAN 	(distanceToBlinky ?distanceFromPacman))
	(test (and 
		(<= ?il 0) (<= ?bl 0) 		;; They are not in the lair (neither shield nor shieldee)
		(<= ?be 0) (>= ?ie 0)		;; The shield is not edible and the shielder is edible
		(< ?distanceFromPacman (+ (/ ?ie 2) 30))	;; And is not orbiting
		(< ?distanceFromPacman (* (/ ?dis 3) 2))	;; And is reachable by us first
		(eq ?shield "")							;; And our shield doesnt need to protect anyone else
	))
	=>
		(modify ?blinkyFact (myShield "PINKY"))
)

(defrule PINKYsavesINKY
	(declare (salience 120))
	(PINKY 	(edibleTime ?be) (lairTime ?bl) (distanceToInky ?dis))
	?inkyFact <- (INKY (edibleTime ?ie) (lairTime ?il) (myShield ?shield))
	(MSPACMAN 	(distanceToInky ?distanceFromPacman))
	(test (and 
		(<= ?il 0) (<= ?bl 0) 		;; They are not in the lair (neither shield nor shieldee)
		(<= ?be 0) (>= ?ie 0)		;; The shield is not edible and the shielder is edible
		(< ?distanceFromPacman (+ (/ ?ie 2) 30))	;; And is not orbiting
		(< ?distanceFromPacman (* (/ ?dis 3) 2))	;; And is reachable by us first
		(eq ?shield "")							;; And our shield doesnt need to protect anyone else
	))
	=>
		(modify ?inkyFact (myShield "PINKY"))
)

(defrule PINKYsavesSUE
	(declare (salience 120))
	(PINKY 	(edibleTime ?be) (lairTime ?bl) (distanceToSue ?dis))
	?sueFact <- (SUE (edibleTime ?ie) (lairTime ?il) (myShield ?shield))
	(MSPACMAN 	(distanceToSue ?distanceFromPacman))
	(test (and 
		(<= ?il 0) (<= ?bl 0) 		;; They are not in the lair (neither shield nor shieldee)
		(<= ?be 0) (>= ?ie 0)		;; The shield is not edible and the shielder is edible
		(< ?distanceFromPacman (+ (/ ?ie 2) 30))	;; And is not orbiting
		(< ?distanceFromPacman (* (/ ?dis 3) 2))	;; And is reachable by us first
		(eq ?shield "")							;; And our shield doesnt need to protect anyone else
	))
	=>
		(modify ?sueFact (myShield "PINKY"))
)

(defrule BLINKYHunter1
	(declare (salience 50))
	(BLINKY (lairTime ?t))
  	(BLINKY (distanceToPacman ?blinkyDistance)) 	; Hecho para la distancia de Blinky
	(PINKY 	(distanceToPacman ?pinkyDistance))   	; Hecho para la distancia de Pinky
	(INKY  	(distanceToPacman ?inkyDistance))     	; Hecho para la distancia de Inky
	(SUE	(distanceToPacman ?sueDistance))       	; Hecho para la distancia de Sue
	?roles <- (ROLES (hunter1 ?h1) (hunter2 ?h2))
	(test (eq ?h1 NONE))
	(test (<= ?t 0))
	(test
		(and (<= ?blinkyDistance ?pinkyDistance)
			(<= ?blinkyDistance ?inkyDistance)
			(<= ?blinkyDistance ?sueDistance)
		)
	)
	=>
	
	(modify ?roles (hunter1 BLINKY))
)

(defrule PINKYHunter1
	(declare (salience 50))
	(PINKY (lairTime ?t))
	(BLINKY (distanceToPacman ?blinkyDistance))
	(PINKY  (distanceToPacman ?pinkyDistance))
	(INKY   (distanceToPacman ?inkyDistance))
	(SUE    (distanceToPacman ?sueDistance))
	?roles <- (ROLES (hunter1 ?h1) (hunter2 ?h2))

	(test (eq ?h1 NONE))
	(test (<= ?t 0))
	(test (and (<= ?pinkyDistance ?blinkyDistance)
				(<= ?pinkyDistance ?inkyDistance)
				(<= ?pinkyDistance ?sueDistance)))
	=>
	(modify ?roles (hunter1 PINKY))
)

(defrule INKYHunter1
	(declare (salience 50))
	(INKY (lairTime ?t))
	(BLINKY (distanceToPacman ?blinkyDistance))
	(PINKY  (distanceToPacman ?pinkyDistance))
	(INKY   (distanceToPacman ?inkyDistance))
	(SUE    (distanceToPacman ?sueDistance))
	?roles <- (ROLES (hunter1 ?h1) (hunter2 ?h2))

	(test (eq ?h1 NONE))
	(test (<= ?t 0))
	(test (and (<= ?inkyDistance ?blinkyDistance)
				(<= ?inkyDistance ?pinkyDistance)
				(<= ?inkyDistance ?sueDistance)))
	=>
	(modify ?roles (hunter1 INKY))
)

(defrule SUEHunter1
	(declare (salience 50))
	(SUE (lairTime ?t))
	(BLINKY (distanceToPacman ?blinkyDistance))
	(PINKY  (distanceToPacman ?pinkyDistance))
	(INKY   (distanceToPacman ?inkyDistance))
	(SUE    (distanceToPacman ?sueDistance))
	?roles <- (ROLES (hunter1 ?h1) (hunter2 ?h2))

	(test (eq ?h1 NONE))
	(test (<= ?t 0))
	(test (and (<= ?sueDistance ?blinkyDistance)
				(<= ?sueDistance ?pinkyDistance)
				(<= ?sueDistance ?inkyDistance)))
	=>
	(modify ?roles (hunter1 SUE))
)

(defrule BLINKYHunter2
	(declare (salience 49))
	(BLINKY (lairTime ?t))
  	(BLINKY (distanceToPacman ?blinkyDistance)) 
	(PINKY 	(distanceToPacman ?pinkyDistance))   
	(INKY  	(distanceToPacman ?inkyDistance))     
	(SUE	(distanceToPacman ?sueDistance))
	?roles <- (ROLES (hunter1 ?h1) (hunter2 ?h2))    
	(test (neq ?h1 NONE))
	(test (eq ?h2 NONE))   
	(test (neq ?h1 BLINKY))
	(test (<= ?t 0))
	(test
		(and 
			(or (eq ?h1 PINKY) (<= ?blinkyDistance ?pinkyDistance))
			(or (eq ?h1 INKY)  (<= ?blinkyDistance ?inkyDistance))
			(or (eq ?h1 SUE)   (<= ?blinkyDistance ?sueDistance))
		)
   	)
	=>
	(modify ?roles (hunter2 BLINKY))
)

(defrule PINKYHunter2
	(declare (salience 48))
	(PINKY (lairTime ?t))
  	(BLINKY (distanceToPacman ?blinkyDistance)) 
	(PINKY 	(distanceToPacman ?pinkyDistance))   
	(INKY  	(distanceToPacman ?inkyDistance))     
	(SUE	(distanceToPacman ?sueDistance))
	?roles <- (ROLES (hunter1 ?h1) (hunter2 ?h2))   
	(test (neq ?h1 NONE))  
	(test (eq ?h2 NONE)) 
	(test (neq ?h1 PINKY))  
	(test (<= ?t 0))
	(test
		(and 
			(or (eq ?h1 BLINKY) (<= ?pinkyDistance ?blinkyDistance))
			(or (eq ?h1 INKY)   (<= ?pinkyDistance ?inkyDistance))
			(or (eq ?h1 SUE)    (<= ?pinkyDistance ?sueDistance))
		)
   	)
	=>
	(modify ?roles (hunter2 PINKY))
)

(defrule INKYHunter2
	(declare (salience 47))
	(INKY (lairTime ?t))
  	(BLINKY (distanceToPacman ?blinkyDistance)) 
	(PINKY 	(distanceToPacman ?pinkyDistance))   
	(INKY  	(distanceToPacman ?inkyDistance))     
	(SUE	(distanceToPacman ?sueDistance))
	?roles <- (ROLES (hunter1 ?h1) (hunter2 ?h2))   
	(test (neq ?h1 NONE))   
	(test (eq ?h2 NONE))  
	(test (neq ?h1 INKY))
	(test (<= ?t 0))
	(test
		(and 
			(or (eq ?h1 BLINKY) (<= ?inkyDistance ?blinkyDistance))
			(or (eq ?h1 PINKY)  (<= ?inkyDistance ?pinkyDistance))
			(or (eq ?h1 SUE)    (<= ?inkyDistance ?sueDistance))
		)
   	)
	=>
	(modify ?roles (hunter2 INKY))
)

(defrule SUEHunter2
	(declare (salience 46))
	(SUE (lairTime ?t))
  	(BLINKY (distanceToPacman ?blinkyDistance)) 
	(PINKY 	(distanceToPacman ?pinkyDistance))   
	(INKY  	(distanceToPacman ?inkyDistance))     
	(SUE	(distanceToPacman ?sueDistance))
	?roles <- (ROLES (hunter1 ?h1) (hunter2 ?h2))       
	(test (neq ?h1 NONE))
	(test (eq ?h2 NONE)) 
	(test (neq ?h1 SUE))
	(test (<= ?t 0))
	(test
		(and 
			(or (eq ?h1 BLINKY) (<= ?sueDistance ?blinkyDistance))
			(or (eq ?h1 PINKY)  (<= ?sueDistance ?pinkyDistance))
			(or (eq ?h1 INKY)   (<= ?sueDistance ?inkyDistance))
		)
   	)
	=>
	(modify ?roles (hunter2 SUE))
)

;; LAIR ;;
(defrule PINKYinlair
	(declare (salience 100))
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
	(declare (salience 21))
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
	(declare (salience 20))
	(PINKY (myShield ?g) (edibleTime ?e))
	(test (> ?e 0))
	(test (neq ?g ""))
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
	(declare (salience 19))
	(PINKY (distanceToPacman ?d) (lairTime ?t) (edibleTime ?e))
	(test (> ?e 0))
	(test (and (= ?t 0) (< ?d 200)))  ;; near if distance < 200
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
	(declare (salience 18))
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
	(declare (salience 17))
	(INKY (myShield ?i))
	(test (eq ?i PINKY))
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
	(declare (salience 17))
	(BLINKY (myShield ?b))
	(test (eq ?b PINKY))
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
	(declare (salience 17))
	(SUE (myShield ?s))
	(test (eq ?s PINKY))
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
	(declare (salience 15))
	(ROLES (hunter1 ?h1))
	(test (eq ?h1 PINKY))
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
	(declare (salience 14))
	(ROLES (hunter1 ?h1) (hunter2 ?h2))
	(test (neq ?h1 NONE))
	(test (eq ?h2 PINKY))
	=>
	(assert 
  		(ACTION 
			(id PINKYHunter2) 
			(info "Soy Hunter2") 
			(extraGhost ?h1) 
			(priority 14)
		)
	)
)

(defrule PINKYNearestToIntersection
	(declare (salience 13))
	(ROLES (hunter1 ?h1) (hunter2 ?h2))
	(MSPACMAN 	(closestIntersection 	?closestintersection))
	(BLINKY 	(distanceToIntersection ?blinkyDistance)) 	
	(PINKY 		(distanceToIntersection ?pinkyDistance))   
	(INKY  		(distanceToIntersection ?inkyDistance))
	(SUE		(distanceToIntersection ?sueDistance))  
	(test 	(or 
				(eq BLINKY ?h1)
				(eq BLINKY ?h2)
				(<= ?pinkyDistance ?blinkyDistance)
			)
	)
	(test 	(or 
				(eq INKY ?h1)
				(eq INKY ?h2)
				(<= ?pinkyDistance ?inkyDistance)
			)
	)
	(test 	(or 
				(eq SUE ?h1)
				(eq SUE ?h2)
				(<= ?pinkyDistance ?sueDistance)
			)
	)
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

(defrule Random
	(declare (salience -1))
	=>
	(assert 
		(ACTION 
			(id PINKYRandom) 
			(info "Random move")  
			(priority 12) 
		)
	)
)