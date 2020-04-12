# OS-2019-2020
Пример програми


## Networking

### TCP
Програмата се наоѓа во `mk.ukim.finki.os.networking.tcp`.
Функционира на следниов начин:
- Стартува `Server` на `localhost` порта `8000`
- Стартува 10 тест `Client` кои праќаат по едно барање
- За секое барање од клиентот, крева нов `Worker` thread и остава тој да го опслужи барањето, и повторно чека ново барање
- Секој `Worker` го чита барањето на корисникот, и враќа одговор
- Секој `Client` откако ќе прати, чека одговор и го принта на екран
- Потоа, имаме можност ние да генерираме наше барање со наши параметри

#### Browser Test

Стартнете ја апликцијата, и со вашиот омилен пребарувач (Firefox, Chrome, ...)
внесете го следното URL:
[http://localhost:8000/movies?name=Ace%20Ventura:%20Pet%20Detective](http://localhost:8000/movies?name=Ace%20Ventura:%20Pet%20Detective)

Серверот би требало да ви го врати името на пребарувачот, и филмот што е побаран.
Пробајте со друг пребарувач, би требало да серверот да знае дека побаравте од друг.

#### Telnet Test

Можеме да генерираме барање до серверот и од друга програма.
Имено, може да ја искористиме `telnet` алатката да ни воспостави TCP конекција со серверот, а потоа да го напишеме нашето барање.

Пример:

```bash
$ telnet localhost 8000
Trying ::1...
Connected to localhost.
Escape character is '^]'.
POST /movies/shawshank_redemption
USER: Sasho
Content-Length: 15
Movie-Name: The Shawshank Redemption
Movie-Year: 1994

Here should be the contents of the movie. But from the Content-Length header, this will be cut to 15 bytes.
HTTP/1.1 200 OK

Hello, Sasho!
You requested to POST the resource: 
You sent me: Here should be 

Connection closed by foreign host.
```

Видете и што испринта серверот во терминал.

### UDP

Програмата се наоѓа во `mk.ukim.finki.os.networking.udp`.

Истата функционира на следнот начин:
- `Server`-от постојано чека за порака
- Штом стигне, ја принта содржината, и враќа нова порака до `Client`-от со неговата адреса и порта
- Секој `Client`, генерира една порака, ја праќа, и чека нова порака како одговор

### NC Test

За да го тестираме функционирањето на серверот од друга програма, за UDP може да користиме `nc`.

Пример:

```bash
$ nc -u localhost 8009
Hello There
Hello there! Your address is /127.0.0.1:37441
```

Видете и што испринта серверот во терминал.
