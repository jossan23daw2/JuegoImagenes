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

        textos_imagenes = {
            "bayonetta0": "Tiene un tono oscuro",
            "bayonetta1": "Tiene un tono grisáceo",
            "bayonetta2": "Tiene un tono Blanquecino",
            "bayonetta3": "Tiene un tono Rojizo",
            "donkey0": "El original",
            "donkey1": "Tiene un tono oscuro",
            "donkey2": "Tiene un tono anaranjado",
            "donkey3": "Se parece al oro",
            "sonic0": "Es de los originales",
            "sonic1": "Tiene un colorparecido a la odsidiana",
            "sonic2": "Es mas claro que el original",
            "sonic3": "Es dl mismo color que shadow",
            "canela0": "La original",
            "canela1": "Tiene el mismo color que una piruleta",
            "canela2": "Tiene el mismo color que incineroar",
            "canela3": "Tiene el mismo color que la oscuridad",
            "greninja0": "El original",
            "greninja1": "Parece un chicle de fresa",
            "greninja2": "Tiene el mismo color que un umbreon",
            "greninja3": "Tiene el mismo color que un ghastly",
            "ike0": "El original",
            "ike1": "Tiene el mismo color que sonic",
            "ike2": "Igual que la orinial pero un toque mas oscuro",
            "ike3": "Igual que la original pero con un pequeño cambio en los pantalones",
            "incineroar0": "el original",
            "incineroar1": "Con un color mas parecido a la obsidiana",
            "incineroar2": "Con un color parecido al carbon",
            "incineroar3": "Igual que el orginal pero alrevés",
            "link0": "El original",
            "link1": "Link con un toque mas antiguo",
            "link2": "Link con toque antiguo pero con color mas claro",
            "link3": "Link de nueva generacion con color azulado",
            "steve0": "el original",
            "steve1": "Steve pero juega al golf",
            "steve2": "steve no muerto",
            "steve3": "steve del end",
            "terry0": "el original",
            "terry1": "Terry azulado.",
            "terry2": "Terry con color de un enderman",
            "terry3": "Terry del oro"
        }

        nombre_imagen = query.get("nom", [""])[0]
        texto = textos_imagenes.get(nombre_imagen, "Texto no encontrado para esta imagen.")

        datos = json.dumps({"nom": texto})
        self.wfile.write(datos.encode("utf-8"))


def run(server_class=HTTPServer, handler_class=ClasseGestora, port=8000):
    server_address = ('', port)
    httpd = server_class(server_address, handler_class)
    print('Iniciando servidor HTTP en el puerto', port)
    httpd.serve_forever()

if __name__ == "__main__":
    from sys import argv
    if len(argv) == 2:
        run(port=int(argv[1]))
    else:
        run()
