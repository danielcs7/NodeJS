const express = require('express')
const nunjucks = require('nunjucks')
const app = express()

// configura o nunjucks
nunjucks.configure('views', {
  autoescape: true,
  express: app,
  watch: true
})

app.use(express.urlencoded({ extended: false }))

app.set('view engine', 'njk')

const checkAgeQueryParam = (req, res, next) => {
  const { age } = req.query

  if (!age) {
    return res.redirect('/')
  }

  return next()
}

const logMiddleware = (req, res, next) => {
  console.log(
    `HOST:  ${req.headers.host} | URL: ${req.url} | METHOD: ${req.method}`
  )

  req.appName = 'GoNode'

  return next()
}

// cria a rota raiz
app.get('/', (req, res) => {
  return res.render('new')
})

// rota check
app.post('/check', (req, res) => {
  // const vIdade = req.body.age
  const { age } = req.body

  console.log(age)

  if (age > 18) {
    // return res.redirect('/major/:age=' + vIdade)
    return res.redirect(`/major?age=${age}`)
  } else {
    return res.redirect(`/minor?age=${age}`)
  }

  //
})

app.get('/major', checkAgeQueryParam, (req, res) => {
  const { age } = req.query

  return res.render('major', { age })
})

app.get('/minor', checkAgeQueryParam, (req, res) => {
  const { age } = req.query

  return res.render('minor', { age })
})
app.listen(3000)
