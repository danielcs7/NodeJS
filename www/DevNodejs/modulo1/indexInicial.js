const express = require('express')

const app = express()

// começo de tudo!!!!

const logMiddleware = (req, res, next) => {
  console.log(
    `HOST:  ${req.headers.host} | URL: ${req.url} | METHOD: ${req.method}`
  )

  req.appName = 'GoNode'

  return next()
}

app.use(logMiddleware)

app.get('/', (req, res) => {
  return res.send(`Bem-Vindo ap , ${req.appName},||${req.query.name} `)
  // sreturn res.send(`checkdate, ${req.query.checkdate}`);
  // return res.send(`hors, ${req.query.hors}`);
  //  return res.send(`temperature, ${req.query.temperature}`);
  //  return res.send(`humidity, ${req.query.humidity}`);

  // return res.send('Hello dev');
})

app.get('/nome/:name', (req, res) => {
  return res.json({
    message: `Bem Vindo, ${req.params.name}`
  })
})

app.get('/arduino/', (req, res) => {
  // para essa requisao utiliza
  // http://localhost:3000/?estufa=07&checkdate=2018-11-21

  // return res.send(`checkdate, ${req.query.checkdate}`);
  // return res.send(`hors, ${req.query.hors}`);
  // return res.send(`temperature, ${req.query.temperature}`);
  // return res.send('Hello dev')

  return res.send(`Estufa, ${req.query.estufa}, || ${req.query.checkdate} `)
})

app.listen(3000)
