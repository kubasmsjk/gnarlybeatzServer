# gnarlybeatzServer

gnarlybeatzServer to serwer aplikacji webowej w technologi Java przy użyciu Spring Boot oraz Hibernate.  
Projekt powstał w ramach pracy inżynierskiej w oparciu o platformę Java. Aplikacja skoncentrowana jest
na sprzedaży i zarządzaniu podkładów muzycznych.

## Podstawowe funkcje aplikacji:
### Sprzedawca:
* możliwość dodawania, archiwizacji, edycji różnych podkładów muzycznych z uwzględnieniem gatunku, tempa (bmp),
* lease (wynajem podkładu z względu na wersję):
	- wersja standard - plik .mp3 (niższa jakość), zawiera tag producencki, sprzedający może dalej czerpać korzyści finansowe z podkładu muzycznego,
	- wersja delux - plik .wav (wyższa jakość), nie zawiera tagu producenckiego, sprzedający może dalej czerpać korzyści finansowe z podkładu muzycznego,
	- wersja exclusive - plik .wav (wyższa jakość), nie zawiera tagu producenckiego, stems ( rozdzielone tracki ) spakowany w winrar/zip, sprzedający nie może czerpać korzyści finansowych z podkładu muzycznego

### Użytkownik:
* możliwość tworzenia i edycji profilu użytkownika,
* możliwość przesłuchania podkładów przed zakupem,
* system płatności online,
* możliwość wyszukiwania podkładów muzycznych według gatunku, tempa(bpm),
* możliwość tworzenia i zarządzania listami ulubionych podkładów muzycznych,
* wysyłanie wiadomości przez formularz kontaktowy

### Serwer:
*	uwierzytelnianie - przy pomocy Jwt Baerer token,
*	przesyłanie plików muzycznych,
*	obsługa płatności online,
*	wysyłanie licencji w pdf poprzez e-mail

## Autor:
* Jakub Mieczkowski ([@kubasmsjk]( https://github.com/kubasmsjk))
