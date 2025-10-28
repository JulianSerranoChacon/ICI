;FACTS ASSERTED BY GAME INPUT
(deftemplate BLINKY
	(slot edible (type SYMBOL)))
	
(deftemplate INKY
	(slot edible (type SYMBOL)))
	
(deftemplate PINKY
	(slot edible (type SYMBOL)))

(deftemplate SUE
	(slot edible (type SYMBOL)))
	
(deftemplate MSPACMAN 
    (slot mindistancePPill (type NUMBER)) 
	(slot hayFantasmasCerca (type SYMBOL))
	(slot soloUnaInterseccionPosible (type SYMBOL))
	(slot variosCaminos (type NUMBER))
	(slot quedanPPils (type SYMBOL))
	(slot estoyCercaDePpil (type SYMBOL))
	(slot hayFantasmasCercaDePpil(type SYMBOL))
	(slot tiempoDesdePpil (type NUMBER))
	(slot distanceToBLINKY (type NUMBER))
	(slot distanceToINKY (type NUMBER))
	(slot distanceToPINKY (type NUMBER))
	(slot distanceToSUE (type NUMBER))
	)
    
;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER) ) ; mandatory slots
	(slot runawaystrategy (type SYMBOL)) ; Extra slot for the runaway action
) 

;RULES 
(defrule MSPacManMoveToClosestPill
	(MSPACMAN (voyGreedy ?d)) (test (?d == true))
	=>
	(assert
		(
			ACTION (id Goes to nearest pill action) (info "Soy un greedy") (priority 0)
		)
	)
)
(defrule MSPacManGetMorePoints
	(MSPACMAN (HayPillEnCaminoInmediato ?d)) (test (?d != 0))
	=>
	(assert
		(
			ACTION (id Greedy Points Action) (info "A por más puntos") (priority 1)
		)
	)
)
(defrule MSPacManEscapeFromAll
	(MSPACMAN (hayFantasmasNoComestiblesCerca ?s)) (test (?s > 0))
	=>
	(assert
		(
			ACTION (id Basic Action) (info "Huyo En general") (priority 2)
		)
	)
)

(defrule MSPacManEscapeFromOne
	(MSPACMAN (hayVariosFantasmasNoComestiblesCerca ?d)) (test (?d == 1))
	=>
	(assert
		(
			ACTION (id Hide From One Action) (info "Huyo de un fantasma") (priority 3)
		)
	)
)
(defrule MSPacManTryPPIL
	(MSPACMAN (hayVariosFantasmasNoComestiblesCerca ?d)) (test (?d > 1))
	=>
	(assert
		(
			ACTION (id Go to PPill Action) (info "IntentoAcercarmeAunaPPIL") (priority 3)
		)
	)
)

	