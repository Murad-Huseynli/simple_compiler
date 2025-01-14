$("document").ready(function()
{
  $(".button").on("click", function() {
    code = $(".code").val();

    document.getElementById("l5").setAttribute("style", "color: red !important;");

    document.getElementById("output").innerHTML = "The output of the code";
  });
});



const textarea = document.querySelector('textarea')
const lineNumbers = document.querySelector('.line-numbers')

textarea.addEventListener('keyup', event => {
  const numberOfLines = event.target.value.split('\n').length

  lines = "";
  for (let i = 1; i <= numberOfLines; i++)
    lines += "<span id = \"l" + i + "\" style = \"color: whitesmoke;\"></span>";
  
  lineNumbers.innerHTML = lines;  
})

textarea.addEventListener('keydown', event => {
  if (event.key === 'Tab') {
    const start = textarea.selectionStart
    const end = textarea.selectionEnd

    textarea.value = textarea.value.substring(0, start) + '\t' + textarea.value.substring(end)

    event.preventDefault()
  }
})
