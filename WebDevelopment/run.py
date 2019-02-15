from app import create_app
from meinheld import server

app = create_app()

if __name__ == "__main__":
	app.run()
