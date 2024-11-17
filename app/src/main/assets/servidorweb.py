from http.server import BaseHTTPRequestHandler, HTTPServer
from urllib.parse import parse_qs
import json

class ClasseGestora(BaseHTTPRequestHandler):
    def _set_headers(self):
        self.send_response(200)
        self.send_header('Content-type', 'application/json')
        self.end_headers()

    def do_GET(self):
        self._set_headers()
        path, _, query_string = self.path.partition('?')
        query = parse_qs(query_string)

        # Diccionario de textos asociados a cada nombre de imagen
        textos_imagenes = {
            "bayonetta0": ".",
            "bayonetta1": "Bayonetta, lista para la batalla.",
            "bayonetta2": "Bayonetta, con un aire elegante y mortal.",
            "bayonetta3": "Bayonetta, desatada con su arsenal mágico.",
            "donkey0": "Donkey Kong, el rey de la selva.",
            "donkey1": "Donkey Kong, un héroe siempre listo.",
            "donkey2": "Donkey Kong, disfrutando de una banana.",
            "donkey3": "Donkey Kong, demostrando su fuerza.",
            "sonic0": "Sonic, el erizo más rápido del mundo.",
            "sonic1": "Sonic, corriendo hacia la victoria.",
            "sonic2": "Sonic, el héroe azul en acción.",
            "sonic3": "Sonic, siempre en movimiento.",
            "canela0": "Canela, siempre servicial y amigable.",
            "canela1": "Canela, lista para ayudarte.",
            "canela2": "Canela, con su actitud cálida y optimista.",
            "canela3": "Canela, la mejor asistente.",
            "greninja0": "Greninja, el maestro ninja de las sombras.",
            "greninja1": "Greninja, rápido y letal.",
            "greninja2": "Greninja, preparado para el combate.",
            "greninja3": "Greninja, un verdadero estratega.",
            "ike0": "Ike, un guerrero con un fuerte sentido del honor.",
            "ike1": "Ike, empuñando su poderosa espada.",
            "ike2": "Ike, demostrando su fuerza en batalla.",
            "ike3": "Ike, un líder nato.",
            "incineroar0": "Incineroar, el luchador felino.",
            "incineroar1": "Incineroar, listo para un combate intenso.",
            "incineroar2": "Incineroar, con su fuego característico.",
            "incineroar3": "Incineroar, el campeón del ring.",
            "link0": "Link, el héroe de Hyrule.",
            "link1": "Link, empuñando la espada maestra.",
            "link2": "Link, listo para salvar el reino.",
            "link3": "Link, siempre valiente y decidido.",
            "steve0": "Steve, el constructor del mundo de Minecraft.",
            "steve1": "Steve, recolectando recursos.",
            "steve2": "Steve, en su aventura minera.",
            "steve3": "Steve, enfrentando los peligros del Nether.",
            "terry0": "Terry, el luchador legendario.",
            "terry1": "Terry, listo para darlo todo.",
            "terry2": "Terry, un maestro de las artes marciales.",
            "terry3": "Terry, el héroe de South Town."
        }

        # Extraer el nombre de la imagen del parámetro 'nom'
        nombre_imagen = query.get("nom", [""])[0]
        texto = textos_imagenes.get(nombre_imagen, "Texto no encontrado para esta imagen.")

        # Responder con el texto en formato JSON
        datos = json.dumps({"Pista": texto})
        self.wfile.write(datos.encode("utf-8"))


# Función para iniciar el servidor
def run(server_class=HTTPServer, handler_class=ClasseGestora, port=8000):
    server_address = ('', port)
    httpd = server_class(server_address, handler_class)
    print('Iniciando servidor HTTP en el puerto', port)
    httpd.serve_forever()

# Ejecutar el servidor
if __name__ == "__main__":
    from sys import argv
    if len(argv) == 2:
        run(port=int(argv[1]))
    else:
        run()
