;FACTS ASSERTED BY GAME INPUT
(deftemplate BLINKY
	(slot edible (type SYMBOL))
	(slot minDistancePacMan (type NUMBER))
	(slot minDistancePpil (type NUMBER))
	(slot estoyCercaYsoyPeligroso (type SYMBOL) (default false))
	(slot llegoAntesAPPil (type SYMBOL)(default false))
)
	
(deftemplate INKY
	(slot edible (type SYMBOL))
	(slot minDistancePacMan (type NUMBER))
	(slot minDistancePpil (type NUMBER))
	(slot estoyCercaYsoyPeligroso (type SYMBOL) (default false)) 
	(slot llegoAntesAPPil (type SYMBOL)(default false))
)
	
(deftemplate PINKY
	(slot edible (type SYMBOL))
	(slot minDistancePacMan (type NUMBER))
	(slot minDistancePpil (type NUMBER))
	(slot estoyCercaYsoyPeligroso (type SYMBOL) (default false)) 
	(slot llegoAntesAPPil (type SYMBOL)(default false))
)

(deftemplate SUE
	(slot edible (type SYMBOL))
	(slot minDistancePacMan (type NUMBER))
	(slot minDistancePpil (type NUMBER))
	(slot estoyCercaYsoyPeligroso (type SYMBOL) (default false))
	(slot llegoAntesAPPil (type SYMBOL)(default false))
)
	
(deftemplate MSPACMAN 
 	(slot voyGreedy (type SYMBOL) (default true)) 
	(slot hayPillEnCaminoInmediato (type SYMBOL)) 
    (slot minDistancePPill (type NUMBER)) 
	(slot variosCaminos (type NUMBER))
	(slot quedanPPils (type SYMBOL))
	(slot tiempoDesdePpil (type NUMBER))

	(slot distanceToBLINKY (type NUMBER))
	(slot distanceToINKY (type NUMBER))
	(slot distanceToPINKY (type NUMBER))
	(slot distanceToSUE (type NUMBER))

	(slot dangerDistanceGhost (type NUMBER))

	(slot distanceToEatBLINKY (type NUMBER))
	(slot distanceToEatINKY (type NUMBER))
	(slot distanceToEatPINKY (type NUMBER))
	(slot distanceToEatSUE (type NUMBER))

	(slot numEatableGhost (type NUMBER) (default 0))
	(slot numDangerGhosts (type NUMBER) (default 0))
	(slot llegoAntesAPPil (type SYMBOL) (default true))
	
	(slot goToPillMove (type SYMBOL))
	
	(slot RIGHTCandidate (type SYMBOL))
	(slot LEFTCandidate (type SYMBOL))
	(slot UPCandidate (type SYMBOL))
	(slot DOWNCandidate (type SYMBOL))
	
	(slot ClosestPpil (type NUMBER))
	
	(slot RIGHTMoveToPpill (type SYMBOL))
	(slot LEFTMoveToPpill (type SYMBOL))
	(slot UPMoveToPpill (type SYMBOL))
	(slot DOWNMoveToPpill (type SYMBOL))
	
	(slot RIGHTMoveToPoints (type NUMBER))
	(slot LEFTMoveToPoints (type NUMBER))
	(slot UPMoveToPoints (type NUMBER))
	(slot DOWNMoveToPoints (type NUMBER))
	
	(slot RIGHTMoveToNode (type NUMBER))
	(slot LEFTMoveToNode (type NUMBER))
	(slot UPMoveToNode (type NUMBER))
	(slot DOWNMoveToNode (type NUMBER))
)
    
;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER) ) ; mandatory slots
	(slot runawaystrategy (type SYMBOL)) ; Extra slot for the runaway action
) 


;TEMPLATES ASIGNADOS EN EL CLP

; Compruebo cuantos fantasmas cercanos no comestibles hay
;BLINKY
(defrule BlinkyNoComestibleYCerca
	(MSPACMAN (dangerDistanceGhost ?d))
	?b <- (BLINKY (edible ?be) (minDistancePacMan ?bd)) 
	(test (?be == false)) and (test (?bd <= ?d))
	=>
	(assert
		(
			 (modify ?b (estoyCercaYsoyPeligroso true))
		)
	)
)
;Añado a Blinky a peligroso si es que este lo es
(defrule BlinkySeConsideraPeligroso
	?m <- (MSPACMAN (numDangerGhosts ?n))
	(BLINKY (estoyCercaYsoyPeligroso true))
	=>
	(assert
		(
			 (modify ?m (numDangerGhosts (?n + 1)))
		)
	)
)

;INKY
(defrule InkyNoComestibleYCerca
	(MSPACMAN (dangerDistanceGhost ?d))
	?b <- (INKY (edible ?be) (minDistancePacMan ?bd)) 
	(test (?be == false)) and (test (?bd <= ?d))
	=>
	(assert
		(
			 (modify ?b (estoyCercaYsoyPeligroso true))
		)
	)
)
;Añado a Inky a peligroso si es que este lo es
(defrule InkySeConsideraPeligroso
	?m <- (MSPACMAN (numDangerGhosts ?n))
	(INKY (estoyCercaYsoyPeligroso true))
	=>
	(assert
		(
			 (modify ?m (numDangerGhosts (?n + 1)))
		)
	)
)

;PINKY
(defrule Pinky
	(MSPACMAN (dangerDistanceGhost ?d))
	?b <- (PINKY (edible ?be) (minDistancePacMan ?bd)) 
	(test (?be == false)) and (test (?bd <= ?d))
	=>
	(assert
		(
			 (modify ?b (estoyCercaYsoyPeligroso true))
		)
	)
)
;Añado a Pinky a peligroso si es que este lo es
(defrule PinkySeConsideraPeligroso
	?m <- (MSPACMAN (numDangerGhosts ?n))
	(PINKY (estoyCercaYsoyPeligroso true))
	=>
	(assert
		(
			 (modify ?m (numDangerGhosts (?n + 1)))
		)
	)
)

;SUE
(defrule SueNoComestibleYCerca
	(MSPACMAN (dangerDistanceGhost ?d))
	?b <- (SUE (edible ?be) (minDistancePacMan ?bd)) 
	(test (?be == false)) and (test (?bd <= ?d))
	=>
	(assert
		(
			 (modify ?b (estoyCercaYsoyPeligroso true))
		)
	)
)
;Añado a Sue a peligroso si es que este lo es
(defrule SueSeConsideraPeligroso
	?m <- (MSPACMAN (numDangerGhosts ?n))
	(SUE (estoyCercaYsoyPeligroso true))
	=>
	(assert
		(
			 (modify ?m (numDangerGhosts (?n + 1)))
		)
	)
)


;COMPROBACION DE QUIEN LLEGA ANTES A LA PPIL
;BLINKY
(defrule BlinkyHaciaPPil
	(MSPACMAN (minDistancePPill ?md))
	?b <- (BLINKY (edible ?e) (minDistancePpil ?d))
	(test ?e == false) and (?bd < ?md)
	=>
	(assert
		(
			 (modify ?b (llegoAntesAPPil true))
		)
	)
)

;Informo a PacMan de que no llega a la PPIL
(defrule BlinkySeComeLaPPil
	?m <- (MSPACMAN (llegoAntesAPPil))
	(BLINKY (llegoAntesAPPil true))
	=>
	(assert
		(
			 (modify ?m (llegoAntesAPPil false))
		)
	)
)
;INKY
(defrule InkyHaciaPPil
	(MSPACMAN (minDistancePPill ?md))
	?b <- (INKY (edible ?e) (minDistancePpil ?d))
	(test ?e == false) and (?bd < ?md)
	=>
	(assert
		(
			 (modify ?b (llegoAntesAPPil true))
		)
	)
)

;Informo a PacMan de que no llega a la PPIL
(defrule InkySeComeLaPPil
	?m <- (MSPACMAN (llegoAntesAPPil))
	(INKY (llegoAntesAPPil true))
	=>
	(assert
		(
			 (modify ?m (llegoAntesAPPil false))
		)
	)
)
;PINKY
(defrule PinkyHaciaPPil
	(MSPACMAN (minDistancePPill ?md))
	?b <- (PINKY (edible ?e) (minDistancePpil ?d))
	(test ?e == false) and (?bd < ?md)
	=>
	(assert
		(
			 (modify ?b (llegoAntesAPPil true))
		)
	)
)

;Informo a PacMan de que no llega a la PPIL
(defrule PinkySeComeLaPPil
	?m <- (MSPACMAN (llegoAntesAPPil))
	(PINKY (llegoAntesAPPil true))
	=>
	(assert
		(
			 (modify ?m (llegoAntesAPPil false))
		)
	)
)
;SUE
(defrule SueHaciaPPil
	(MSPACMAN (minDistancePPill ?md))
	?b <- (SUE (edible ?e) (minDistancePpil ?d))
	(test ?e == false) and (?bd < ?md)
	=>
	(assert
		(
			 (modify ?b (llegoAntesAPPil true))
		)
	)
)

;Informo a PacMan de que no llega a la PPIL
(defrule SueSeComeLaPPil
	?m <- (MSPACMAN (llegoAntesAPPil))
	(SUE (llegoAntesAPPil true))
	=>
	(assert
		(
			 (modify ?m (llegoAntesAPPil false))
		)
	)
)


;COMPROBACIÓN DE CUANTOS FANTASMAS COMESTIBLES HAY
;Blinky
(defrule BlinkySePuedeComer
	?m <- (MSPACMAN (numEatableGhost ?n) (distanceToBLINKY ?d) (distanceToEatBLINKY ?de))
	?b <-(BLINKY (edible ?e))
	test(?e == true) and (?d <= ?de)
	=>
	(assert
		(
			 (modify ?m (numEatableGhost (?n + 1)))
		)
	)
)
;Inky
(defrule InkySePuedeComer
	?m <- (MSPACMAN (numEatableGhost ?n) (distanceToINKY ?d) (distanceToEatINKY ?de))
	?b <-(INKY (edible ?e))
	test(?e == true) and (?d <= ?de)
	=>
	(assert
		(
			 (modify ?m (numEatableGhost (?n + 1)))
		)
	)
)
;Pinky
(defrule PinkySePuedeComer
	?m <- (MSPACMAN (numEatableGhost ?n) (distanceToPINKY ?d) (distanceToEatPINKY ?de))
	?b <-(PINKY (edible ?e))
	test(?e == true) and (?d <= ?de)
	=>
	(assert
		(
			 (modify ?m (numEatableGhost (?n + 1)))
		)
	)
)
;Sue
(defrule SueSePuedeComer
	?m <- (MSPACMAN (numEatableGhost ?n) (distanceToSUE ?d) (distanceToEatSUE ?de))
	?b <-(SUE (edible ?e))
	test(?e == true) and (?d <= ?de)
	=>
	(assert
		(
			 (modify ?m (numEatableGhost (?n + 1)))
		)
	)
)

;RULES 
; Comienzo siempre en ir a comer pills, luego iré viendo que tengo que hacer realmente
(defrule MSPacManMoveToClosestPill
	(MSPACMAN (voyGreedy true)) ;(test (?s == true))
	=>
	(assert
		(
			ACTION (id Goes to nearest pill action) (info "Soy un greedy") (priority 0)
		)
	)
)

; Si hay pills inmediatas voy a por ellas
(defrule MSPacManGetMorePoints
	(MSPACMAN (hayPillEnCaminoInmediato true))
	=>
	(assert
		(
			ACTION (id Greedy Points Action) (info "A por más puntos") (priority 1)
		)
	)
)

; Si solo hay un fantasma cerca de mi huyo de el
(defrule MSPacManEscapeFromOne
	MSPACMAN(numDangerGhosts 1)
	=>
	(assert
		(
			ACTION (id Hide From One Action) (info "Huyo de un fantasma") (priority 2)
		)
	)
)

; Si hay muchos fantasmas cerca de mi, intento comerme la PPIL

(defrule MSPacManTryPPIL
	(MSPACMAN (numDangerGhosts ?n))(test ?n > 1)
	=>
	(assert
		(
			ACTION (id Go to PPill Action) (info "IntentoAcercarmeAunaPPIL") (priority 3)
		)
	)
)

; Si tengo muchos fantasmas persiguiendome y llego a la powerPill voy a por ella

(defrule MSPacManEatPPIL
	(MSPACMAN (llegoAntesAPPil ?b) (numDangerGhosts ?n) (minDistancePPill ?m))
	(test(?b == true)) and (?n > 1) and (?m <= 50) ;50 puesto a ojo

	=>
	(assert
		(
			ACTION (id Eat PPill Action) (info "Me he comido una PPIl") (priority 4)
		)
	)
)

; Si hay fantasmas comestibles cerca y no hay fantasmas no comestibles cerca me voy a comerlos 
(defrule MSPacManStartsFollowing
	(MSPACMAN (numDangerGhosts ?nd) (numEatableGhost ?ne) )
	(test (?nd == 0)) and (test (?ne > 0))
	(assert
		(
			ACTION (id Chase Action) (info "voy a comer") (priority 5)
		)
	)
)

;Si no hay caminos disponibles intento ir a por la PPIL
;FALTA COMPROBAR SI LLEGO A COMERLA
(defrule MSPacManStartSuicida
	(MSPACMAN (variosCaminos 0)) 
	=>
	(assert
		(
			ACTION (id PPill Suicida) (info "Intento huir a la powerPPil") (priority 6)
		)
	)
)

; Si no hay caminos disponibles, ni PPILS pero hay pils accesibles, me muevo a esas pills
(defrule MSPacManPillsSuicida
	(MSPACMAN (variosCaminos 0, quedanPPils 0))
	=>
	(assert
		(
			ACTION (id Go to pills suicida action) (info "A pillar la mayor cantidad de puntos que pueda") (priority 7)
		)
	)
)

;Si no hay caminos disponibles, ni PPILS, ni PILLS me muevo random
(defrule MSPacManRandom
	(MSPACMAN (variosCaminos 0, quedanPPils 0,hayPillEnCaminoInmediato false)) 
	=>
	(assert
		(
			ACTION (id Random Action) (info "Todo al verde") (priority 8)
		)
	)
)
;Si solo se puede mover a un lado da igual todo lo demás debemos ir a ese camino
(defrule MSPacManSoloUnMovimiento
	(MSPACMAN (variosCaminos 1))
	=>
	(assert
		(
			ACTION (id Only move possible action) (info "Solo tengo un movimiento posible") (priority 19)
		)
	)
)	