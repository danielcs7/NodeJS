module.exports = (req, res, next) => {
  if (req.session && !req.session.user) {
    // prossegui com a requisição
    return next()
  }

  // retorno pro login
  return res.redirect('/app/dashboard')
}
