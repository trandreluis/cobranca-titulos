$('#confirmacaoExclusaoModal').on(
		'show.bs.modal',
		function(event) {

			var botaoExcluir = $(event.relatedTarget);
			var idTitulo = botaoExcluir.data('id');
			var descricaoTitulo = botaoExcluir.data('descricao');

			var modal = $(this);
			var form = modal.find('form');
			var action = form.data('url-base');

			if (!action.endsWith('/')) {
				action += '/';
			}

			form.attr('action', action + idTitulo);

			modal.find('.modal-body span').html(
					'Tem certeza que deseja excluir o título <strong>'
							+ descricaoTitulo + '</strong>?');

		});

$(function() {
	$('[rel="tooltip"]').tooltip();
	$('.js-currency').maskMoney({
		decimal : ',',
		thousands : '.',
		allowZero : true
	});

	$('.js-atualizar-status').on('click', function(event) {
		event.preventDefault();
		var botaoReceber = $(event.currentTarget);
		var urlReceber = botaoReceber.attr('href');
		
		var response = $.ajax({
			url: urlReceber,
			type: 'PUT'
		});
	
		response.done(function(e) {
			var idTitulo = botaoReceber.data('id');
			$('[data-role=' + idTitulo + ']').html('<span class="label label-success">'+e+'</span>');
			botaoReceber.hide();
		});
		
		response.fail(function(e) {
			console.log(e);
			alert('Erro ao tentar receber');
		});
		
	});
});