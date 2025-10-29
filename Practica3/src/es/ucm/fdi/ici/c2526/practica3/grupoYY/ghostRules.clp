;; DEFINITION OF DATA TYPES ;;
;;(deftemplate BLINKY
;;	(slot edible (type SYMBOL)))
	
;;(deftemplate INKY
;;	(slot edible (type SYMBOL)))
	
;;(deftemplate PINKY
;;	(slot edible (type SYMBOL)))

;;(deftemplate SUE
;;	(slot edible (type SYMBOL)))
	
(deftemplate MSPACMAN 
    (slot mindistancePPill (type NUMBER)) )
 
;;NEW DATA TYPES

;;  ADD DISTANCE FROM GHOST TO PACMAN ;;

(deftemplate BLINKYtoPacman
	(slot distanceTo (type FLOAT)))   ;;I dont know if this should be float
   
(deftemplate INKYtoPacman
	(slot distanceTo (type FLOAT)))   ;;I dont know if this should be float   
  
(deftemplate PINKYtoPacman
	(slot distanceTo (type FLOAT)))   ;;I dont know if this should be float
	
(deftemplate SUEtoPacman
	(slot distanceTo (type FLOAT)))   ;;I dont know if this should be float  

;;  ADD DISTANCE FROM PACMAN TO GHOST ;;

(deftemplate PacmanToBLINKY
	(slot distanceTo (type FLOAT)))   
   
(deftemplate PacmanToINKY
	(slot distanceTo (type FLOAT)))   
  
(deftemplate PacmanToPINKY
	(slot distanceTo (type FLOAT)))   
	
(deftemplate PacmanToSUE
	(slot distanceTo (type FLOAT)))    

	;;  ADD DISTANCE FROM GHOST TO NEXT PACMAN INTERSECTION ;;

(deftemplate BLINKYToIntersection
	(slot distanceTo (type FLOAT)))   
   
(deftemplate INKYToIntersection
	(slot distanceTo (type FLOAT)))   
  
(deftemplate PINKYToIntersection
	(slot distanceTo (type FLOAT)))   
	
(deftemplate SUEToIntersection
	(slot distanceTo (type FLOAT)))    


;; DISTANCE FROM GHOST TO GHOST ;;

;;BLINKY
(deftemplate BLINKYToINKY
	(slot distanceTo (type FLOAT)))    

(deftemplate BLINKYToPINKY
	(slot distanceTo (type FLOAT)))  
	
(deftemplate BLINKYToSUE
	(slot distanceTo (type FLOAT)))  

;;INKY	
(deftemplate INKYToBLINKY
	(slot distanceTo (type FLOAT)))    

(deftemplate INKYToPINKY
	(slot distanceTo (type FLOAT)))  
	
(deftemplate INKYToSUE
	(slot distanceTo (type FLOAT)))  

;;PINKY
(deftemplate PINKYToBLINKY
	(slot distanceTo (type FLOAT)))    

(deftemplate PINKYToINKY
	(slot distanceTo (type FLOAT)))  
	
(deftemplate PINKYToSUE
	(slot distanceTo (type FLOAT)))  

;;SUE
(deftemplate SUEToBLINKY
	(slot distanceTo (type FLOAT)))    

(deftemplate SUEToINKY
	(slot distanceTo (type FLOAT)))  
	
(deftemplate SUEToPINKY
	(slot distanceTo (type FLOAT)))  

;; GHOST ROLES ;;

(deftemplate BLINKYrole
	(slot role (type SYMBOL)))  

(deftemplate INKYrole
	(slot role (type SYMBOL))) 

(deftemplate PINKYrole
	(slot role (type SYMBOL))) 

(deftemplate SUErole
	(slot role (type SYMBOL))) 

;; SHIELD GHOST ;;

(deftemplate shieldGhost
	(slot ghost (type SYMBOL))
	(slot covers (type SYMBOL))
) 
	
;; EDIBLE TIME GHOST ;;

(deftemplate BLINKYedible
	(slot edibleTime (type NUMBER))) 

(deftemplate INKYedible
	(slot edibleTime (type NUMBER))) 
	
(deftemplate PINKYedible
	(slot edibleTime (type NUMBER))) 
	
(deftemplate SUEedible
	(slot edibleTime (type NUMBER))) 
	
;; EDIBLE TIME GHOST ;;

(deftemplate BLINKYlair
	(slot lairTime (type NUMBER))) 

(deftemplate INKYlair
	(slot lairTime (type NUMBER))) 
	
(deftemplate PINKYlair
	(slot lairTime (type NUMBER))) 
	
(deftemplate SUElair
	(slot lairTime (type NUMBER))) 
 
;; DEFINITION OF THE ACTION FACT (ALSO A DATA_TYPE lol) --> IS ALL IN THE PERSPECTIVE OF BLINKY, WE WILL ADAPT TO OTHER GHOSTS ;;

;; Basic action
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER) ) ; mandatory slots
	(slot runawaystrategy (type SYMBOL)) ; Extra slot for the runaway action
) 

;; Hunter1 ACTION
(deftemplate Hunter1ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER)))

;; Hunter2 ACTION --> I just need to know the ghost hunter 1, not the entire map
(deftemplate Hunter2ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER) )
	(slot hunter1Id (type SYMBOL)))
	
;; RunToEscudero ACTION --> I just need to know the ghost hunter 1, not the entire map
(deftemplate EscuderoACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER) )
	(slot ghostQueCubre (type SYMBOL)))
	
(deftemplate EscuderoACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER)) )

;; RULES OF ALL GHOSTS --> IS ALL IN THE PERSPECTIVE OF BLINKY, WE WILL ADAPT TO OTHER GHOSTS ;;

(defrule BLINKYrunsAway
	(BLINKY (edible true)) 
	=>  
	(assert 
		(ACTION (id BLINKYrunsAway) (info "Comestible --> huir") (priority 23) 
			(runawaystrategy CORNER)
		)
	)
)

(defrule BLINKYpacmanNear
   (PacmanToBLINKY (distanceTo ?d))
   (BLINKYlair (lairTime ?t))
   (BLINKYedible (edibleTime ?e))
   (test (or (== ?t 0) (< ?d 200)))  ;; near if distance < 200
=>
   (assert
      (ACTION 
         (id BLINKYOrbit)
         (info "BLINKY near and edible")
         (priority 22) 		;;Reassign priority
      )
   )
)

;;(defrule BLINKYhaPasadoEscudero prioridad 21

(defrule BLINKYhayEscudero
	( shieldGhost (ghost ?g) (covers BLINKY))
	=>
   (assert
      (EscuderoACTION 
         (id RunToEscuderoAction)
         (info "BLINKY going to escudero")
         (ghostQueCubre ?g)
         (priority 20) 		;;Reassign priority
      )
   )
)

(defrule BLINKYpacmanFarAway
   (PacmanToBLINKY (distanceTo ?d))
   (BLINKYlair (lairTime ?t))
   (BLINKYedible (edibleTime ?e))
   (test (or (!= ?t 0) (> ?d (+ (/ ?e 2) 1))))  ;; far away if distance > (edibleTime/2 + 1)
=>
   (assert
      (ACTION 
         (id BLINKYOrbit)
         (info "BLINKY far away and edible")
         (priority 19) 		;;Reassign priority
      )
   )
)

(defrule BLINKYrunsAwayMSPACMANclosePPill
	(MSPACMAN (mindistancePPill ?d)) (test (<= ?d 30)) 
	=>  
	(assert 
		(ACTION (id BLINKYrunsAway) (info "MSPacMan cerca PPill") (priority 18) 
			(runawaystrategy RANDOM)
		)
	)
)

(defrule BLINKYediblesNearPacman
	(PINKYtoPacman (distanceTo ?pinkyDistance))   ; Hecho para la distancia de Pinky
  	(INKYtoPacman (distanceTo ?inkyDistance))     ; Hecho para la distancia de Inky
  	(SUEtoPacman (distanceTo ?sueDistance))       ; Hecho para la distancia de Sue
	(BLINKYedible (edibleTime ?blinkyEdible))
	(INKYedible (edibleTime ?INKYEdible))
	(SUEedible (edibleTime ?SUEEdible))
	(PINKYedible (edibleTime ?PINKYEdible))

	(and
		(or
			(and (PINKYedible (edibleTime ?PINKYEdible&:(> ?PINKYEdible 0)))
				(PINKYtoPacman (distanceTo ?pinkyDistance&:(< ?pinkyDistance 100))))
			(and (INKYedible (edibleTime ?INKYEdible&:(> ?INKYEdible 0)))
				(INKYtoPacman (distanceTo ?inkyDistance&:(< ?inkyDistance 100))))
			(and (SUEedible (edibleTime ?SUEEdible&:(> ?SUEEdible 0)))
				(SUEtoPacman (distanceTo ?sueDistance&:(< ?sueDistance 100))))
    )
	=>
	(assert (ACTION (id "BLINKYrunToTheEdible") (info "me vuelvo escudero") (priority 17)
)

(defrule BLINKYnoediblesNearPacman ; HAY QUE CAMBIAR SEGUN EL FANTASMA QUE SEAS
	(PINKYtoPacman (distanceTo ?pinkyDistance))   ; Hecho para la distancia de Pinky
  	(INKYtoPacman (distanceTo ?inkyDistance))     ; Hecho para la distancia de Inky
  	(SUEtoPacman (distanceTo ?sueDistance))       ; Hecho para la distancia de Sue
	(BLINKYedible (edibleTime ?blinkyEdible))
	(INKYedible (edibleTime ?INKYEdible))
	(SUEedible (edibleTime ?SUEEdible))
	(PINKYedible (edibleTime ?PINKYEdible))

	(not
		(or
			(and (PINKYedible (edibleTime ?PINKYEdible&:(> ?PINKYEdible 0)))
				(PINKYtoPacman (distanceTo ?pinkyDistance&:(< ?pinkyDistance 100))))
			(and (INKYedible (edibleTime ?INKYEdible&:(> ?INKYEdible 0)))
				(INKYtoPacman (distanceTo ?inkyDistance&:(< ?inkyDistance 100))))
			(and (SUEedible (edibleTime ?SUEEdible&:(> ?SUEEdible 0)))
				(SUEtoPacman (distanceTo ?sueDistance&:(< ?sueDistance 100))))
		)
	)
	=>
	(assert (ACTION (id "BLINKYrunToTheEdible") (info "me vuelvo escudero") (priority 16)
)

(defrule BLINKYNearestToMsPacman ; HAY QUE CAMBIAR SEGUN EL FANTASMA QUE SEAS
  (BLINKYtoPacman (distanceTo ?blinkyDistance)) ; Hecho para la distancia de Blinky
  (PINKYtoPacman (distanceTo ?pinkyDistance))   ; Hecho para la distancia de Pinky
  (INKYtoPacman (distanceTo ?inkyDistance))     ; Hecho para la distancia de Inky
  (SUEtoPacman (distanceTo ?sueDistance))       ; Hecho para la distancia de Sue
  (test (<= ?blinkyDistance ?pinkyDistance))
  (test (<= ?blinkyDistance ?inkyDistance))
  (test (<= ?blinkyDistance ?sueDistance))
	=> 
	(assert (ACTION (id Hunter1) (info "Soy cazador1")  (priority 15) ))
)

(defrule BLINKYSecondNearestToMsPacman
  (BLINKYtoPacman (distanceTo ?blinkyDistance)) ; Hecho para la distancia de Blinky
  (PINKYtoPacman (distanceTo ?pinkyDistance))   ; Hecho para la distancia de Pinky
  (INKYtoPacman (distanceTo ?inkyDistance))     ; Hecho para la distancia de Inky
  (SUEtoPacman (distanceTo ?sueDistance))       ; Hecho para la distancia de Sue
  (test (<= ?blinkyDistance ?pinkyDistance))
  (test (<= ?blinkyDistance ?inkyDistance))
  (test (<= ?blinkyDistance ?sueDistance))
  =>
  (assert (ACTION (id Hunter2) (info "Soy Hunter2") (priority 14)))
)

(defrule BLINKYNearestToIntersection
  (BLINKYToIntersection (distanceTo ?blinkyDistance)) ; Hecho para la distancia de Blinky
  (PINKYToIntersection (distanceTo ?pinkyDistance))   ; Hecho para la distancia de Pinky
  (INKYToIntersection (distanceTo ?inkyDistance))     ; Hecho para la distancia de Inky
  (SUEToIntersection (distanceTo ?sueDistance))       ; Hecho para la distancia de Sue
  (test (<= ?blinkyDistance ?pinkyDistance))
  (test (<= ?blinkyDistance ?inkyDistance))
  (test (<= ?blinkyDistance ?sueDistance))
	=> 
	(assert (ACTION (id JailerAction) (info "Soy Jailer")  (priority 13) ))
)

(defrule BLINKYrandom ; HAY QUE CAMBIAR SEGUN EL FANTASMA QUE SEAS
	(assert (ACTION (id ) (info "BLINKYRandom")  (priority 12) ))
)	
