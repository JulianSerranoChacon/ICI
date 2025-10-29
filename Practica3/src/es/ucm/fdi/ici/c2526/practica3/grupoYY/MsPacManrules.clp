;FACTS ASSERTED BY GAME INPUT
(deftemplate BLINKY
	(slot edible (type SYMBOL))
	(slot BLINKYMinDistancePacMan (type NUMBER))
	(slot BLINKYMinDistancePpil (type NUMBER))
)
	
(deftemplate INKY
	(slot edible (type SYMBOL))
	(slot INKYMinDistancePacMan (type NUMBER))
	(slot INKYMinDistancePpil (type NUMBER))
)
	
(deftemplate PINKY
	(slot edible (type SYMBOL))
	(slot PINKYMinDistancePacMan (type NUMBER))
	(slot PINKYMinDistancePpil (type NUMBER))
)

(deftemplate SUE
	(slot edible (type SYMBOL))
	(slot SUEMinDistancePacMan (type NUMBER))
	(slot PINKYMinDistancePpil (type NUMBER))
)
	
(deftemplate MSPACMAN 
 	(slot voyGreedy (type SYMBOL)) 
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
	)
    
;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER) ) ; mandatory slots
	(slot runawaystrategy (type SYMBOL)) ; Extra slot for the runaway action
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

; Si hay fantasmas no comestibles cerca intento huir
(defrule MSPacManEscapeFromAll
	(MSPACMAN (dangerDistanceGhost ?d))
	(BLINKY (edible ?be) (BLINKYMinDistancePacMan ?bd)) 
	(INKY (edible ?ie) (INKYMinDistancePacMan ?id)) 
	(PINKY (edible ?pe) (PINKYMinDistancePacMan ?pd)) 
	(SUE (edible ?se) (SUEMinDistancePacMan ?ps))
	(test (?be == false)) and (test (?bd <= ?d)) or	(test (?ie == false)) and (test (?id <= ?d)) 	
	(test (?pe == false)) and (test (?pd <= ?d)) or (test (?se == false)) and (test (?sd <= ?d))
	=>
	(assert
		(
			ACTION (id Basic Action) (info "Huyo En general") (priority 2)
		)
	)
)

; Si solo hay un fantasma cerca de mi huyo de el
(defrule MSPacManEscapeFromOne
	(MSPACMAN (dangerDistanceGhost ?d))
	(BLINKY (edible ?be) (BLINKYMinDistancePacMan ?bd)) 
	(INKY (edible ?ie) (INKYMinDistancePacMan ?id)) 
	(PINKY (edible ?pe) (PINKYMinDistancePacMan ?pd)) 
	(SUE (edible ?se) (SUEMinDistancePacMan ?ps))
	;BLINKY EL ÚNICO CERCANO
	(test (?be == false)) and (test (?bd <= ?d)) and (test (?ie == true)) or (test (?id > ?d)) and 
	(test (?pe == true)) or (test (?pd > ?d)) and (test (?se == true)) or (test (?sd > ?d))
	;INKY EL ÚNICO CERCANO 
	(test (?be == true)) or (test (?bd > ?d)) and (test (?ie == false)) and (test (?id <= ?d)) and 
	(test (?pe == true)) or (test (?pd > ?d)) and (test (?se == true)) or (test (?sd > ?d)) 
	;PINKY EL ÚNICO CERCANO
	(test (?be == true)) or (test (?bd > ?d)) and (test (?ie == true)) or (test (?id > ?d)) and 
	(test (?pe == false)) and (test (?pd <= ?d)) and (test (?se == true)) or (test (?sd > ?d)) 
	;SUE EL ÚNICO CERCANO
	(test (?be == true)) or (test (?bd > ?d)) and (test (?ie == true)) or (test (?id > ?d)) and 
	(test (?pe == true)) or (test (?pd > ?d)) and (test (?se == false)) and (test (?sd <= ?d)) 
	=>
	(assert
		(
			ACTION (id Hide From One Action) (info "Huyo de un fantasma") (priority 3)
		)
	)
)

; Si hay muchos fantasmas cerca de mi, intento comerme la PPIL

(defrule MSPacManTryPPIL
	(MSPACMAN (dangerDistanceGhost ?d))
	(BLINKY (edible ?be) (BLINKYMinDistancePacMan ?bd)) 
	(INKY (edible ?ie) (INKYMinDistancePacMan ?id)) 
	(PINKY (edible ?pe) (PINKYMinDistancePacMan ?pd)) 
	(SUE (edible ?se) (SUEMinDistancePacMan ?ps))
	;BLINKY ES UN FANTASMA NO COMIBLE Y HAY ALGUN MÁS MÍNIMO
	(test (?be == false)) and (test (?bd <= ?d)) and (test (?ie == false)) and (test (?id <= ?d)) or 
	(test (?be == false)) and (test (?bd <= ?d)) and (test (?pe == false)) and (test (?pd <= ?d)) or 
	(test (?be == false)) and (test (?bd <= ?d)) and (test (?se == false)) and (test (?sd <= ?d)) or
	;INKY ES UN FANTASMA NO COMIBLE Y HAY ALGUN MÁS MÍNIMO
	(test (?ie == false)) and (test (?id <= ?d)) and (test (?pe == false)) and (test (?pd <= ?d)) or 
	(test (?ie == false)) and (test (?id <= ?d)) and (test (?se == false)) and (test (?sd <= ?d)) or
	;PINKY ES UN FANTASMA NO COMIBLE Y HAY ALGUN MÁS MÍNIMO
	(test (?pe == false)) and (test (?pd <= ?d)) and (test (?se == false)) and (test (?sd <= ?d)) 
	=>
	(assert
		(
			ACTION (id Go to PPill Action) (info "IntentoAcercarmeAunaPPIL") (priority 3)
		)
	)
)

; Si tengo muchos fantasmas persiguiendome y llego a la powerPill voy a por ella

(defrule MSPacManEatPPIL
	(MSPACMAN (dangerDistanceGhost ?d) (minDistancePPill ?m))
	(BLINKY (edible ?be) (BLINKYMinDistancePacMan ?bd)) 
	(INKY (edible ?ie) (INKYMinDistancePacMan ?id)) 
	(PINKY (edible ?pe) (PINKYMinDistancePacMan ?pd)) 
	(SUE (edible ?se) (SUEMinDistancePacMan ?ps))

	;EL 50 ESTA PUESTO A MANO

	(test (?be == false)) and (test (?bd <= ?d)) and (test (?ie == false)) and (test (?id <= ?d)) and ;Si BLINKY E INKY ESTAN CERCA Y NO SON COMESTIBLES
	(test (?m < 50)) and (test (?m < ?b)) and (test (?m < ?i)) and (test (?m < ?p)) and (test (?m < ?s)) or ;SI LLEGO A COMERME LA PPIL ANTES QUE LOS FANTASMAS Y ESTOY CERCA DE LA MISMA

	(test (?be == false)) and (test (?bd <= ?d)) and (test (?pe == false)) and (test (?pd <= ?d)) and ;Si BLINKY Y PINKY ESTAN CERCA Y NO SON COMESTIBLES
	(test (?m < 50)) and (test (?m < ?b)) and (test (?m < ?i)) and (test (?m < ?p)) and (test (?m < ?s)) or ;SI LLEGO A COMERME LA PPIL ANTES QUE LOS FANTASMAS Y ESTOY CERCA DE LA MISMA

	(test (?be == false)) and (test (?bd <= ?d)) and (test (?se == false)) and (test (?sd <= ?d)) and ;Si BLINKY Y SUE ESTAN CERCA Y NO SON COMESTIBLES
	(test (?m < 50)) and (test (?m < ?b)) and (test (?m < ?i)) and (test (?m < ?p)) and (test (?m < ?s)) or  ;SI LLEGO A COMERME LA PPIL ANTES QUE LOS FANTASMAS Y ESTOY CERCA DE LA MISMA

	(test (?ie == false)) and (test (?id <= ?d)) and (test (?pe == false)) and (test (?pd <= ?d)) and ;Si INKY Y PINKY ESTAN CERCA Y NO SON COMESTIBLES
	(test (?m < 50)) and (test (?m < ?b)) and (test (?m < ?i)) and (test (?m < ?p)) and (test (?m < ?s)) or  ;SI LLEGO A COMERME LA PPIL ANTES QUE LOS FANTASMAS Y ESTOY CERCA DE LA MISMA
	
	(test (?ie == false)) and (test (?id <= ?d)) and (test (?se == false)) and (test (?sd <= ?d)) and ;SI INKY Y SUE ESTAN CERCA Y NO SON COMESTIBLES
	(test (?m < 50)) and (test (?m < ?b)) and (test (?m < ?i)) and (test (?m < ?p)) and (test (?m < ?s)) or  ;SI LLEGO A COMERME LA PPIL ANTES QUE LOS FANTASMAS Y ESTOY CERCA DE LA MISMA

	(test (?pe == false)) and (test (?pd <= ?d)) and (test (?se == false)) and (test (?sd <= ?d)) and ;SI PINKY Y SUE ESTAN CERCA Y NO SON COMESTIBLES
	(test (?m < 50)) and (test (?m < ?b)) and (test (?m < ?i)) and (test (?m < ?p)) and (test (?m < ?s)) or  ;SI LLEGO A COMERME LA PPIL ANTES QUE LOS FANTASMAS Y ESTOY CERCA DE LA MISMA
	
	=>
	(assert
		(
			ACTION (id Eat PPill Action) (info "Me he comido una PPIl") (priority 4)
		)
	)
)

;TODO: IMPLEMENTAR LA LÓGICA QUE HACER PARA IR A COMER
; Si hay fantasmas comestibles cerca y no hay fantasmas no comestibles cerca me voy a comerlos 
(defrule MSPacManStartsFollowing
	(MSPACMAN (dangerDistanceGhost ?d) (distanceToBLINKY ?db) (distanceToINKY ?di) (distanceToPINKY ?dp) (distanceToSUE ?ds))
	(BLINKY (edible ?be) (BLINKYMinDistancePacMan ?bd)) 
	(INKY (edible ?ie) (INKYMinDistancePacMan ?id)) 
	(PINKY (edible ?pe) (PINKYMinDistancePacMan ?pd)) 
	(SUE (edible ?se) (SUEMinDistancePacMan ?ps))
	
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