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
 	(slot voyGreedy (type SYMBOL)) 
	(slot HayPillEnCaminoInmediato (type NUMBER)) 
    (slot mindistancePPill (type NUMBER)) 
	(slot hayFantasmasNoComestiblesCerca (type SYMBOL))
	(slot hayVariosFantasmasNoComestiblesCerca (type NUMBER))
	(slot hayVariosFantasmasComestiblesCerca (type NUMBER))
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
; Comienzo siempre en ir a comer pills, luego iré viendo que tengo que hacer realmente
(defrule MSPacManMoveToClosestPill
	(MSPACMAN (voyGreedy ?d)) (test (?d == true))
	=>
	(assert
		(
			ACTION (id Goes to nearest pill action) (info "Soy un greedy") (priority 0)
		)
	)
)

; Si hay pills inmediatas voy a por ellas
(defrule MSPacManGetMorePoints
	(MSPACMAN (HayPillEnCaminoInmediato ?d)) (test (?d != 0))
	=>
	(assert
		(
			ACTION (id Greedy Points Action) (info "A por más puntos") (priority 1)
		)
	)
)

; Si hay fantasmas no comestibles cerca intento huir
(defrule MSPacManEscapeFromAll
	(MSPACMAN (hayFantasmasNoComestiblesCerca ?s)) (test (?s > 0))
	=>
	(assert
		(
			ACTION (id Basic Action) (info "Huyo En general") (priority 2)
		)
	)
)

; Si solo hay un fantasma cerca de mi huyo de el
(defrule MSPacManEscapeFromOne
	(MSPACMAN (hayVariosFantasmasNoComestiblesCerca ?d)) (test (?d == 1))
	=>
	(assert
		(
			ACTION (id Hide From One Action) (info "Huyo de un fantasma") (priority 3)
		)
	)
)

; Si hay muchos fantasmas cerca de mi, intento comerme la PPIL

(defrule MSPacManTryPPIL
	(MSPACMAN (hayVariosFantasmasNoComestiblesCerca ?d)) (test (?d > 1))
	=>
	(assert
		(
			ACTION (id Go to PPill Action) (info "IntentoAcercarmeAunaPPIL") (priority 3)
		)
	)
)

(defrule MSPacManEatPPIL
	(MSPACMAN (estoyCercaDePpil ?d)) (mindistancePPill ?s) (test (?d == true)) (test (?s < 50)) ;ESTE VALOR ESTA A OJO HAY QUE CAMBIARLO
	=>
	(assert
		(
			ACTION (id Eat PPill Action) (info "Me he comido una PPIl") (priority 4)
		)
	)
)

; Si hay fantasmas comestibles cerca y no hay fantasmas no comestibles cerca me voy a comerlos 
; FALTA COMPROBAR SI LLEGO A COMERLOS
(defrule MSPacManStartsFollowing
	(MSPACMAN (hayVariosFantasmasNoComestiblesCerca ?d) (hayVariosFantasmasComestiblesCerca ?c)) (test (?d == 0)) && (test (?c > 0 ))
	=>
	(assert
		(
			ACTION (id Chase Action) (info "voy a comer") (priority 5)
		)
	)
)

;Si no hay caminos disponibles intento ir a por la PPIL
;FALTA COMPROBAR SI LLEGO A COMERLA
(defrule MSPacManStartSuicida
	(MSPACMAN (variosCaminos ?s)) (test (?s == 0 ))
	=>
	(assert
		(
			ACTION (id PPill Suicida) (info "Intento huir a la powerPPil") (priority 6)
		)
	)
)

; Si no hay caminos disponibles, ni PPILS pero hay pils accesibles, me muevo a esas pills
(defrule MSPacManPillsSuicida
	(MSPACMAN (variosCaminos ?s, quedanPPils ?f)) (test (?s == 0 && ?f == 0 ))
	=>
	(assert
		(
			ACTION (id Go to pills suicida action) (info "A pillar la mayor cantidad de puntos que pueda") (priority 7)
		)
	)
)

;Si no hay caminos disponibles, ni PPILS, ni PILLS me muevo random
(defrule MSPacManRandom
	(MSPACMAN (variosCaminos ?s, quedanPPils ?f,HayPillEnCaminoInmediato ?d)) (test (?s == 0 && ?f == 0 && ?d == 0 ))
	=>
	(assert
		(
			ACTION (id Random Action) (info "Todo al verde") (priority 8)
		)
	)
)
;Si solo se puede mover a un lado da igual todo lo demás debemos ir a ese camino
(defrule MSPacManSoloUnMovimiento
	(MSPACMAN (variosCaminos ?d)) (test (?d == 1))
	=>
	(assert
		(
			ACTION (id Only move possible action) (info "Solo tengo un movimiento posible") (priority 19)
		)
	)
)

	